package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.modules.auth.model.User;
import br.gov.noronha.sistur.modules.social.model.Itinerary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedItineraryDTO {
    private Long id;
    private String name;
    private boolean isPublic;
    private String shareToken;
    private LocalDateTime createdAt;
    private int viewCount;
    private SharedUserDTO user;
    private List<SharedItineraryItemDTO> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SharedUserDTO {
        private Long id;
        private String name;
        private String photoUrl;
    }

    public static SharedItineraryDTO fromEntity(Itinerary itinerary) {
        User user = itinerary.getUser();

        return SharedItineraryDTO.builder()
                .id(itinerary.getId())
                .name(itinerary.getName())
                .isPublic(itinerary.isPublic())
                .shareToken(itinerary.getShareToken())
                .createdAt(itinerary.getCreatedAt())
                .viewCount(itinerary.getViews())
                .user(user == null ? null : SharedUserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .photoUrl(user.getPhotoUrl())
                        .build())
                .items(itinerary.getItems() == null ? List.of() : itinerary.getItems().stream()
                        .map(SharedItineraryItemDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
