package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.model.Itinerary;
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
            coverImage = itinerary.getItems().get(0).getImage();
        }

        return ItineraryFeedDTO.builder()
                .id(itinerary.getId())
                .name(itinerary.getName())
                .userName(itinerary.getUser().getName())
                .userPhoto(itinerary.getUser().getPhotoUrl())
                .createdAt(itinerary.getCreatedAt())
                .views(itinerary.getViews())
                .likes(likes)
                .commentsCount(commentsCount)
                .userLiked(userLiked)
                .coverImage(coverImage)
                .build();
    }
}
