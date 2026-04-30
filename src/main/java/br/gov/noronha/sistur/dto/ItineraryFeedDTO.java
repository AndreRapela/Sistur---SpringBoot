package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.modules.social.model.Itinerary;
import br.gov.noronha.sistur.modules.social.model.ItineraryItem;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ItineraryFeedDTO {
    private Long id;
    private String name;
    private String userName;
    private String userPhoto;
    private String shareToken;
    private LocalDateTime createdAt;
    private int views;
    private long likes;
    private long commentsCount;
    private boolean userLiked;
    // Just the first image for the feed
    private String coverImage;

    public static ItineraryFeedDTO fromEntity(Itinerary itinerary, long likes, long commentsCount, boolean userLiked) {
        String coverImage = null;
        if (itinerary.getItems() != null && !itinerary.getItems().isEmpty()) {
            coverImage = itinerary.getItems().stream()
                    .findFirst()
                    .map(ItineraryItem::getImage)
                    .orElse(null);
        }

        return ItineraryFeedDTO.builder()
                .id(itinerary.getId())
                .name(itinerary.getName())
                .userName(itinerary.getUser().getName())
                .userPhoto(itinerary.getUser().getPhotoUrl())
                .shareToken(itinerary.getShareToken())
                .createdAt(itinerary.getCreatedAt())
                .views(itinerary.getViews())
                .likes(likes)
                .commentsCount(commentsCount)
                .userLiked(userLiked)
                .coverImage(coverImage)
                .build();
    }
}
