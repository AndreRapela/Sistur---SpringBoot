package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.modules.social.model.ItineraryItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedItineraryItemDTO {
    private String referenceId;
    private String type;
    private String name;
    private String image;
    private String location;
    private String category;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private int day;
    private String time;
    private String notes;

    public static SharedItineraryItemDTO fromEntity(ItineraryItem item) {
        return SharedItineraryItemDTO.builder()
                .referenceId(item.getReferenceId())
                .type(item.getType())
                .name(item.getName())
                .image(item.getImage())
                .location(item.getLocation())
                .category(item.getCategory())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .day(item.getDay())
                .time(item.getScheduledTime())
                .notes(item.getNotes())
                .build();
    }
}
