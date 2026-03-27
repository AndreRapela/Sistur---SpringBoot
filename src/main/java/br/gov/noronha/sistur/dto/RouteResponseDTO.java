package br.gov.noronha.sistur.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class RouteResponseDTO {
    private long distanceMeters;
    private long durationSeconds;
    private String polyline; // For frontend Leaflet/Google map
    private String difficulty; // EASY, MODERATE, HARD
    private double estimatedCalories;
    private List<LocationDTO> optimizedWaypoints;
}
