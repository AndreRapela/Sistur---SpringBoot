package br.gov.noronha.sistur.modules.social.service;

import br.gov.noronha.sistur.modules.social.model.*;
import br.gov.noronha.sistur.modules.auth.model.User;
import br.gov.noronha.sistur.modules.social.repository.ItineraryRepository;
import br.gov.noronha.sistur.modules.social.repository.ItineraryLikeRepository;
import br.gov.noronha.sistur.modules.social.repository.ItineraryCommentRepository;
import br.gov.noronha.sistur.modules.social.repository.projection.ItineraryCountProjection;
import br.gov.noronha.sistur.modules.gamification.service.GamificationService;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import br.gov.noronha.sistur.dto.SharedItineraryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final UserRepository userRepository;
    private final GamificationService gamificationService;
    private final ItineraryLikeRepository likeRepository;
    private final ItineraryCommentRepository commentRepository;

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<br.gov.noronha.sistur.dto.ItineraryFeedDTO> getFeed(Pageable pageable, String currentEmail) {
        User currentUser = null;
        if (currentEmail != null && !currentEmail.equals("anonymousUser")) {
            currentUser = userRepository.findByEmail(currentEmail).orElse(null);
        }

        Page<Itinerary> page = itineraryRepository.findByIsPublicTrue(pageable);
        List<Itinerary> itineraries = page.getContent();
        List<Long> itineraryIds = itineraries.stream()
            .map(Itinerary::getId)
            .toList();

        if (itineraryIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, page.getTotalElements());
        }

        Map<Long, Long> likesByItinerary = toCountMap(
            likeRepository.countLikesByItineraryIds(itineraryIds)
        );
        Map<Long, Long> commentsByItinerary = toCountMap(
            commentRepository.countCommentsByItineraryIds(itineraryIds)
        );

        Set<Long> likedItineraryIds = new HashSet<>();
        if (currentUser != null && !itineraryIds.isEmpty()) {
            likedItineraryIds.addAll(
                likeRepository.findItineraryIdsLikedByUserAndItineraryIds(currentUser.getId(), itineraryIds)
            );
        }

        List<br.gov.noronha.sistur.dto.ItineraryFeedDTO> feedItems = itineraries.stream()
            .map(itin -> br.gov.noronha.sistur.dto.ItineraryFeedDTO.fromEntity(
                itin,
                likesByItinerary.getOrDefault(itin.getId(), 0L),
                commentsByItinerary.getOrDefault(itin.getId(), 0L),
                likedItineraryIds.contains(itin.getId())
            ))
            .toList();

        return new PageImpl<>(feedItems, pageable, page.getTotalElements());
    }

    @Transactional
    public void toggleLike(Long itineraryId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow(() -> new RuntimeException("Itinerary not found"));

        likeRepository.findByItineraryIdAndUserId(itineraryId, user.getId())
                .ifPresentOrElse(
                        likeRepository::delete,
                        () -> likeRepository.save(ItineraryLike.builder()
                                .itinerary(itinerary)
                                .user(user)
                                .build())
                );
    }

    @Transactional
    public ItineraryComment addComment(Long itineraryId, String email, String content) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow(() -> new RuntimeException("Itinerary not found"));

        ItineraryComment comment = ItineraryComment.builder()
                .itinerary(itinerary)
                .user(user)
                .content(content)
                .build();
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<ItineraryComment> getComments(Long itineraryId) {
        return commentRepository.findByItineraryIdOrderByCreatedAtDesc(itineraryId);
    }

    @Transactional
    public SharedItineraryDTO getSharedItinerary(String token) {
        Itinerary itinerary = itineraryRepository.findByShareToken(token)
                .orElseGet(() -> resolveItineraryById(token));

        itinerary.setViews(itinerary.getViews() + 1);
        itineraryRepository.save(itinerary);

        return SharedItineraryDTO.fromEntity(itinerary);
    }

    @Transactional(readOnly = true)
    public List<Itinerary> getMyItineraries(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return itineraryRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Page<Itinerary> getPublicItineraries(Pageable pageable) {
        return itineraryRepository.findByIsPublicTrue(pageable);
    }

    @Transactional(readOnly = true)
    public List<Itinerary> getPublicItineraries() {
        return itineraryRepository.findByIsPublicTrue();
    }

    @Transactional
    public Itinerary save(Itinerary itinerary, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        itinerary.setUser(user);
        if (itinerary.getShareToken() == null || itinerary.getShareToken().isBlank()) {
            itinerary.setShareToken(UUID.randomUUID().toString());
        }
        if (itinerary.getItems() != null) {
            for (ItineraryItem item : itinerary.getItems()) {
                item.setItinerary(itinerary);
            }
        }

        Itinerary saved = itineraryRepository.save(itinerary);
        gamificationService.checkAndAwardBadges(user.getId());
        return saved;
    }

    public void delete(Long id, String email) {
        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        if (!itinerary.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Permission denied");
        }

        itineraryRepository.delete(itinerary);
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

    private Itinerary resolveItineraryById(String token) {
        try {
            Long itineraryId = Long.valueOf(token);
            return itineraryRepository.findById(itineraryId)
                    .orElseThrow(() -> new NoSuchElementException("Roteiro compartilhado não encontrado."));
        } catch (NumberFormatException ex) {
            throw new NoSuchElementException("Roteiro compartilhado não encontrado.");
        }
    }
}
