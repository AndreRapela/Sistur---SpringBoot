package br.gov.noronha.sistur.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class LocationDTO {
    private Double lat;
    private Double lng;
    private String name;
}
