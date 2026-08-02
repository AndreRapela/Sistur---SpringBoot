package br.gov.noronha.sistur.modules.social.service;

import br.gov.noronha.sistur.dto.ItineraryFeedDTO;
import br.gov.noronha.sistur.dto.ItineraryCommentDTO;
import br.gov.noronha.sistur.dto.ItineraryItemRequest;
import br.gov.noronha.sistur.dto.ItinerarySaveRequest;
import br.gov.noronha.sistur.dto.SharedItineraryDTO;
import br.gov.noronha.sistur.exception.ForbiddenOperationException;
import br.gov.noronha.sistur.exception.ResourceNotFoundException;
import br.gov.noronha.sistur.modules.auth.model.User;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import br.gov.noronha.sistur.modules.gamification.service.GamificationService;
import br.gov.noronha.sistur.modules.social.model.Itinerary;
import br.gov.noronha.sistur.modules.social.model.ItineraryComment;
import br.gov.noronha.sistur.modules.social.model.ItineraryItem;
import br.gov.noronha.sistur.modules.social.model.ItineraryLike;
import br.gov.noronha.sistur.modules.social.repository.ItineraryCommentRepository;
import br.gov.noronha.sistur.modules.social.repository.ItineraryLikeRepository;
import br.gov.noronha.sistur.modules.social.repository.ItineraryRepository;
import br.gov.noronha.sistur.modules.social.repository.projection.ItineraryCountProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final UserRepository userRepository;
    private final GamificationService gamificationService;
    private final ItineraryLikeRepository likeRepository;
    private final ItineraryCommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Page<ItineraryFeedDTO> getFeed(Pageable pageable, String currentEmail) {
        User currentUser = currentEmail == null ? null : userRepository.findByEmail(currentEmail).orElse(null);
        Page<Itinerary> page = itineraryRepository.findByIsPublicTrue(pageable);
        List<Long> itineraryIds = page.getContent().stream().map(Itinerary::getId).toList();
        if (itineraryIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, page.getTotalElements());
        }

        Map<Long, Long> likesByItinerary = toCountMap(likeRepository.countLikesByItineraryIds(itineraryIds));
        Map<Long, Long> commentsByItinerary = toCountMap(commentRepository.countCommentsByItineraryIds(itineraryIds));
        Set<Long> likedItineraryIds = new HashSet<>();
        if (currentUser != null) {
            likedItineraryIds.addAll(likeRepository.findItineraryIdsLikedByUserAndItineraryIds(currentUser.getId(), itineraryIds));
        }

        List<ItineraryFeedDTO> items = page.getContent().stream()
            .map(itinerary -> ItineraryFeedDTO.fromEntity(
                itinerary,
                likesByItinerary.getOrDefault(itinerary.getId(), 0L),
                commentsByItinerary.getOrDefault(itinerary.getId(), 0L),
                likedItineraryIds.contains(itinerary.getId())
            ))
            .toList();
        return new PageImpl<>(items, pageable, page.getTotalElements());
    }

    @Transactional
    public void toggleLike(Long itineraryId, String email) {
        User user = requireUser(email);
        Itinerary itinerary = requirePublicItinerary(itineraryId);
        likeRepository.findByItineraryIdAndUserId(itineraryId, user.getId())
            .ifPresentOrElse(
                likeRepository::delete,
                () -> likeRepository.save(ItineraryLike.builder().itinerary(itinerary).user(user).build())
            );
    }

    @Transactional
    public ItineraryCommentDTO addComment(Long itineraryId, String email, String content) {
        User user = requireUser(email);
        Itinerary itinerary = requirePublicItinerary(itineraryId);
        ItineraryComment comment = ItineraryComment.builder()
            .itinerary(itinerary)
            .user(user)
            .content(content.trim())
            .build();
        return ItineraryCommentDTO.fromEntity(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<ItineraryCommentDTO> getComments(Long itineraryId) {
        requirePublicItinerary(itineraryId);
        return commentRepository.findTop50ByItineraryIdOrderByCreatedAtDesc(itineraryId).stream()
            .map(ItineraryCommentDTO::fromEntity)
            .toList();
    }

    @Transactional
    public SharedItineraryDTO getSharedItinerary(String token) {
        validateShareToken(token);
        if (itineraryRepository.incrementViewsByShareToken(token) == 0) {
            throw new ResourceNotFoundException("Roteiro compartilhado não encontrado.");
        }
        return itineraryRepository.findByShareToken(token)
            .map(SharedItineraryDTO::fromEntity)
            .orElseThrow(() -> new ResourceNotFoundException("Roteiro compartilhado não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<SharedItineraryDTO> getMyItineraries(String email) {
        User user = requireUser(email);
        return itineraryRepository.findByUserOrderByCreatedAtDesc(user).stream()
            .map(SharedItineraryDTO::fromEntity)
            .toList();
    }

    @Cacheable("public_itineraries")
    @Transactional(readOnly = true)
    public List<SharedItineraryDTO> getPublicItineraries() {
        return itineraryRepository.findTop100ByIsPublicTrueOrderByCreatedAtDesc().stream()
            .map(SharedItineraryDTO::fromEntity)
            .toList();
    }

    @CacheEvict(value = "public_itineraries", allEntries = true)
    @Transactional
    public SharedItineraryDTO save(ItinerarySaveRequest request, String email) {
        User user = requireUser(email);
        Itinerary itinerary = Itinerary.builder()
            .name(request.name().trim())
            .user(user)
            .isPublic(request.isPublic())
            .createdAt(LocalDateTime.now())
            .shareToken(UUID.randomUUID().toString())
            .items(new ArrayList<>())
            .build();

        if (request.items() != null) {
            for (ItineraryItemRequest itemRequest : request.items()) {
                itinerary.getItems().add(toEntity(itemRequest, itinerary));
            }
        }

        Itinerary saved = itineraryRepository.save(itinerary);
        gamificationService.checkAndAwardBadges(user.getId());
        return SharedItineraryDTO.fromEntity(saved);
    }

    @CacheEvict(value = "public_itineraries", allEntries = true)
    @Transactional
    public void delete(Long id, String email) {
        Itinerary itinerary = itineraryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Roteiro não encontrado."));
        if (!itinerary.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ForbiddenOperationException("Você não pode remover este roteiro.");
        }
        itineraryRepository.delete(itinerary);
    }

    private ItineraryItem toEntity(ItineraryItemRequest request, Itinerary itinerary) {
        return ItineraryItem.builder()
            .itinerary(itinerary)
            .referenceId(request.referenceId().trim())
            .type(request.type())
            .name(request.name().trim())
            .image(blankToNull(request.image()))
            .location(blankToNull(request.location()))
            .category(blankToNull(request.category()))
            .latitude(request.latitude())
            .longitude(request.longitude())
            .day(request.day())
            .scheduledTime(blankToNull(request.time()))
            .notes(blankToNull(request.notes()))
            .build();
    }

    private User requireUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    private Itinerary requirePublicItinerary(Long id) {
        return itineraryRepository.findByIdAndIsPublicTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("Roteiro público não encontrado."));
    }

    private void validateShareToken(String token) {
        try {
            UUID.fromString(token);
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new ResourceNotFoundException("Roteiro compartilhado não encontrado.");
        }
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private Map<Long, Long> toCountMap(List<ItineraryCountProjection> counts) {
        Map<Long, Long> result = new LinkedHashMap<>();
        for (ItineraryCountProjection count : counts) {
            if (count.getItineraryId() != null) {
                result.put(count.getItineraryId(), count.getTotal() == null ? 0L : count.getTotal());
            }
        }
        return result;
    }
}
