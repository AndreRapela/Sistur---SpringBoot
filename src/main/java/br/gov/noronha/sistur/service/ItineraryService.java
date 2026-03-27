package br.gov.noronha.sistur.service;

import br.gov.noronha.sistur.model.Itinerary;
import br.gov.noronha.sistur.model.ItineraryItem;
import br.gov.noronha.sistur.model.User;
import br.gov.noronha.sistur.repository.ItineraryRepository;
import br.gov.noronha.sistur.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final UserRepository userRepository;
    private final GamificationService gamificationService;
    private final br.gov.noronha.sistur.repository.ItineraryLikeRepository likeRepository;
    private final br.gov.noronha.sistur.repository.ItineraryCommentRepository commentRepository;

    public org.springframework.data.domain.Page<br.gov.noronha.sistur.dto.ItineraryFeedDTO> getFeed(Pageable pageable, String currentEmail) {
        User currentUser = null;
        if (currentEmail != null && !currentEmail.equals("anonymousUser")) {
            currentUser = userRepository.findByEmail(currentEmail).orElse(null);
        }

        Page<Itinerary> page = itineraryRepository.findByIsPublicTrue(pageable);
        
        final User finalCurrentUser = currentUser;
        return page.map(itin -> {
            long likes = likeRepository.countByItineraryId(itin.getId());
            long commentsCount = commentRepository.findByItineraryIdOrderByCreatedAtDesc(itin.getId()).size();
            boolean userLiked = false;
            if (finalCurrentUser != null) {
                userLiked = likeRepository.findByItineraryIdAndUserId(itin.getId(), finalCurrentUser.getId()).isPresent();
            }
            return br.gov.noronha.sistur.dto.ItineraryFeedDTO.fromEntity(itin, likes, commentsCount, userLiked);
        });
    }

    @Transactional
    public void toggleLike(Long itineraryId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow(() -> new RuntimeException("Itinerary not found"));

        likeRepository.findByItineraryIdAndUserId(itineraryId, user.getId())
                .ifPresentOrElse(
                        likeRepository::delete,
                        () -> likeRepository.save(br.gov.noronha.sistur.model.ItineraryLike.builder()
                                .itinerary(itinerary)
                                .user(user)
                                .build())
                );
    }

    @Transactional
    public br.gov.noronha.sistur.model.ItineraryComment addComment(Long itineraryId, String email, String content) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow(() -> new RuntimeException("Itinerary not found"));

        br.gov.noronha.sistur.model.ItineraryComment comment = br.gov.noronha.sistur.model.ItineraryComment.builder()
                .itinerary(itinerary)
                .user(user)
                .content(content)
                .build();
        return commentRepository.save(comment);
    }

    public List<br.gov.noronha.sistur.model.ItineraryComment> getComments(Long itineraryId) {
        return commentRepository.findByItineraryIdOrderByCreatedAtDesc(itineraryId);
    }

    public List<Itinerary> getMyItineraries(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return itineraryRepository.findByUser(user);
    }

    public Page<Itinerary> getPublicItineraries(Pageable pageable) {
        return itineraryRepository.findByIsPublicTrue(pageable);
    }

    public List<Itinerary> getPublicItineraries() {
        return itineraryRepository.findByIsPublicTrue();
    }

    @Transactional
    public Itinerary save(Itinerary itinerary, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        itinerary.setUser(user);
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
}
