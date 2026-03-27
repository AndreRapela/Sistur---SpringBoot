package br.gov.noronha.sistur.service;

import br.gov.noronha.sistur.dto.LocationDTO;
import br.gov.noronha.sistur.dto.RouteRequestDTO;
import br.gov.noronha.sistur.dto.RouteResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {

    // Simula a integra??o com a API do Google Directions pedida na Fase 2
    public RouteResponseDTO calculateRoute(RouteRequestDTO request) {
        List<LocationDTO> waypoints = request.getWaypoints();
        
        // 1. Simula??o: Otimiza ordem de waypoints (TSP)
        List<LocationDTO> optimized = optimizeWaypoints(waypoints);

        // 2. Simula??o de C?lculo de Dist?ncia (Harvesine MOCK)
        long totalDistance = calculateMockDistance(optimized);
        
        // Caminhada avg: 1.4 m/s. Carro: ~10 m/s
        long duration = "WALKING".equalsIgnoreCase(request.getTravelMode()) ?
                (long)(totalDistance / 1.4) : (long)(totalDistance / 10.0);

        // 3. Eleva??o / Dificuldade
        int elevationGain = (int) (totalDistance * 0.05); // Mock 5% inclina??o avg
        
        // 4. Cria Resposta
        return RouteResponseDTO.builder()
                .distanceMeters(totalDistance)
                .durationSeconds(duration)
                .difficulty(calculateDifficulty(elevationGain))
                .estimatedCalories(totalDistance * 0.05) // aprox 50 cal per km walked
                .optimizedWaypoints(optimized)
                .polyline("u{f~H~x}o@a@c@?e@@m@@a@Ea@Me@O_@Qa@S[YYWUUOOMKMIKII?G") // Mock Polyline
                .build();
    }

    private List<LocationDTO> optimizeWaypoints(List<LocationDTO> waypoints) {
        // Mocking a TSP optimization by simply returning the original list
        // Real app would make a call to Google Optimization API
        return waypoints.stream().collect(Collectors.toList());
    }

    private long calculateMockDistance(List<LocationDTO> waypoints) {
        long dist = 0;
        for (int i = 0; i < waypoints.size() - 1; i++) {
            LocationDTO p1 = waypoints.get(i);
            LocationDTO p2 = waypoints.get(i + 1);
            // Haversine distance formula mock
            if (p1.getLat() != null && p1.getLng() != null && p2.getLat() != null && p2.getLng() != null) {
                dist += distance(p1.getLat(), p1.getLng(), p2.getLat(), p2.getLng());
            } else {
                dist += 1500; // fall back 1.5km
            }
        }
        return dist == 0 ? 5000 : dist; // fall back 5km se s? tiver 1 ponto
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + 
                      Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        // Metros
        return dist * 1.609344 * 1000;
    }

    private String calculateDifficulty(int totalElevationGain) {
        if (totalElevationGain < 100) return "EASY";
        if (totalElevationGain < 500) return "MODERATE";
        return "HARD";
    }
}
