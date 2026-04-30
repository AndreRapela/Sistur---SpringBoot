package br.gov.noronha.sistur.modules.tourism.service;

import br.gov.noronha.sistur.dto.LocationDTO;
import br.gov.noronha.sistur.dto.RouteRequestDTO;
import br.gov.noronha.sistur.dto.RouteResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouteServiceTest {

    private final RouteService routeService = new RouteService();

    @Test
    void calculateRouteReordersByNearestNeighborAndEncodesPolyline() {
        RouteRequestDTO request = new RouteRequestDTO();
        request.setTravelMode("WALKING");
        request.setWaypoints(List.of(
                waypoint(0.0, 0.0, "A"),
                waypoint(0.0, 2.0, "C"),
                waypoint(0.0, 1.0, "B")
        ));

        RouteResponseDTO response = routeService.calculateRoute(request);

        assertEquals(List.of("A", "B", "C"), response.getOptimizedWaypoints().stream().map(LocationDTO::getName).toList());
        assertTrue(response.getDistanceMeters() > 220_000L);
        assertTrue(response.getDurationSeconds() > 0L);
        assertFalse(response.getPolyline().isBlank());
        assertEquals("HARD", response.getDifficulty());
    }

    @Test
    void calculateRouteReturnsEmptyValuesForSingleWaypoint() {
        RouteRequestDTO request = new RouteRequestDTO();
        request.setTravelMode("DRIVING");
        request.setWaypoints(List.of(waypoint(0.0, 0.0, "Solo")));

        RouteResponseDTO response = routeService.calculateRoute(request);

        assertEquals(0L, response.getDistanceMeters());
        assertEquals(0L, response.getDurationSeconds());
        assertEquals(0.0, response.getEstimatedCalories());
        assertEquals("EASY", response.getDifficulty());
        assertFalse(response.getPolyline().isBlank());
        assertEquals(1, response.getOptimizedWaypoints().size());
    }

    private LocationDTO waypoint(double lat, double lng, String name) {
        return LocationDTO.builder()
                .lat(lat)
                .lng(lng)
                .name(name)
                .build();
    }
}