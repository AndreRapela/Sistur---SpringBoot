package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.modules.social.model.ItineraryItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharedItineraryItemDTO {
    private Long referenceId;
    private String type;
    private String name;
    private String image;
    private String location;
    private int day;

    public static SharedItineraryItemDTO fromEntity(ItineraryItem item) {
        return SharedItineraryItemDTO.builder()
                .referenceId(item.getReferenceId())
                .type(item.getType())
                .name(item.getName())
                .image(item.getImage())
                .location(item.getLocation())
                .day(item.getDay())
                .build();
    }
}