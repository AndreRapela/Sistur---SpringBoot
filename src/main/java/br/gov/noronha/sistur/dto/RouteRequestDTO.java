package br.gov.noronha.sistur.dto;

import lombok.Data;
import java.util.List;

@Data
public class RouteRequestDTO {
    private List<LocationDTO> waypoints;
    private String travelMode; // WALKING, DRIVING, BICYCLING
}
