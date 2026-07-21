package br.gov.noronha.sistur.modules.tourism.service;

import br.gov.noronha.sistur.dto.LocationDTO;
import br.gov.noronha.sistur.dto.RouteRequestDTO;
import br.gov.noronha.sistur.dto.RouteResponseDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class RouteService {

    private static final double EARTH_RADIUS_METERS = 6_371_000.0;
    private static final double WALKING_SPEED_METERS_PER_SECOND = 1.4;
    private static final double BICYCLING_SPEED_METERS_PER_SECOND = 4.5;
    private static final double DRIVING_SPEED_METERS_PER_SECOND = 10.0;
    private static final long FALLBACK_SEGMENT_METERS = 1_500L;

    // Mantem a API local previsivel com uma aproximacao geometricamente consistente.
    public RouteResponseDTO calculateRoute(RouteRequestDTO request) {
        if (request == null || request.getWaypoints() == null || request.getWaypoints().isEmpty()) {
            return emptyRoute();
        }

        List<LocationDTO> waypoints = request.getWaypoints().stream()
                .filter(Objects::nonNull)
                .toList();

        if (waypoints.isEmpty()) {
            return emptyRoute();
        }

        // Ordena por vizinho mais proximo quando ha coordenadas suficientes para isso.
        List<LocationDTO> optimized = optimizeWaypoints(waypoints);

        long totalDistance = calculateRouteDistanceMeters(optimized);
        double speedMetersPerSecond = speedFor(request.getTravelMode());
        long duration = totalDistance == 0 ? 0L : Math.max(1L, Math.round(totalDistance / speedMetersPerSecond));

        int elevationGain = estimateElevationGainMeters(totalDistance, request.getTravelMode());
        
        return RouteResponseDTO.builder()
                .distanceMeters(totalDistance)
                .durationSeconds(duration)
                .difficulty(calculateDifficulty(request.getTravelMode(), totalDistance, elevationGain))
                .estimatedCalories(estimateCalories(totalDistance, request.getTravelMode()))
                .optimizedWaypoints(optimized)
                .polyline(encodePolyline(optimized))
                .build();
    }

    private List<LocationDTO> optimizeWaypoints(List<LocationDTO> waypoints) {
        if (waypoints.size() < 3 || !hasCoordinatesForAllWaypoints(waypoints)) {
            return List.copyOf(waypoints);
        }

        List<LocationDTO> remaining = new ArrayList<>(waypoints);
        List<LocationDTO> optimized = new ArrayList<>(remaining.size());

        LocationDTO current = remaining.remove(0);
        optimized.add(current);

        while (!remaining.isEmpty()) {
            LocationDTO next = findNearestWaypoint(current, remaining);
            optimized.add(next);
            remaining.remove(next);
            current = next;
        }

        return List.copyOf(optimized);
    }

    private long calculateRouteDistanceMeters(List<LocationDTO> waypoints) {
        if (waypoints.size() < 2) {
            return 0L;
        }

        long distanceMeters = 0L;
        for (int i = 0; i < waypoints.size() - 1; i++) {
            LocationDTO p1 = waypoints.get(i);
            LocationDTO p2 = waypoints.get(i + 1);
            if (p1.getLat() != null && p1.getLng() != null && p2.getLat() != null && p2.getLng() != null) {
                distanceMeters += distanceMeters(p1.getLat(), p1.getLng(), p2.getLat(), p2.getLng());
            } else {
                distanceMeters += FALLBACK_SEGMENT_METERS;
            }
        }
        return distanceMeters;
    }

    private LocationDTO findNearestWaypoint(LocationDTO origin, List<LocationDTO> candidates) {
        LocationDTO nearest = candidates.get(0);
        double shortestDistance = distanceMeters(origin.getLat(), origin.getLng(), nearest.getLat(), nearest.getLng());

        for (int i = 1; i < candidates.size(); i++) {
            LocationDTO candidate = candidates.get(i);
            double candidateDistance = distanceMeters(origin.getLat(), origin.getLng(), candidate.getLat(), candidate.getLng());
            if (candidateDistance < shortestDistance) {
                shortestDistance = candidateDistance;
                nearest = candidate;
            }
        }

        return nearest;
    }

    private boolean hasCoordinatesForAllWaypoints(List<LocationDTO> waypoints) {
        return waypoints.stream().allMatch(waypoint -> waypoint.getLat() != null && waypoint.getLng() != null);
    }

    private double distanceMeters(Double lat1, Double lon1, Double lat2, Double lon2) {
        double startLat = Math.toRadians(lat1);
        double endLat = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(startLat) * Math.cos(endLat)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double clamped = Math.min(1.0, Math.max(0.0, a));
        double c = 2 * Math.atan2(Math.sqrt(clamped), Math.sqrt(1 - clamped));

        return EARTH_RADIUS_METERS * c;
    }

    private double speedFor(String travelMode) {
        String mode = normalizeTravelMode(travelMode);
        return switch (mode) {
            case "WALKING" -> WALKING_SPEED_METERS_PER_SECOND;
            case "BICYCLING" -> BICYCLING_SPEED_METERS_PER_SECOND;
            default -> DRIVING_SPEED_METERS_PER_SECOND;
        };
    }

    private int estimateElevationGainMeters(long distanceMeters, String travelMode) {
        String mode = normalizeTravelMode(travelMode);
        double factor = switch (mode) {
            case "WALKING" -> 0.05;
            case "BICYCLING" -> 0.03;
            default -> 0.01;
        };

        return (int) Math.max(0L, Math.round(distanceMeters * factor));
    }

    private double estimateCalories(long distanceMeters, String travelMode) {
        String mode = normalizeTravelMode(travelMode);
        double caloriesPerMeter = switch (mode) {
            case "WALKING" -> 0.05;
            case "BICYCLING" -> 0.03;
            default -> 0.0;
        };

        return distanceMeters * caloriesPerMeter;
    }

    private String calculateDifficulty(String travelMode, long totalDistanceMeters, int totalElevationGain) {
        String mode = normalizeTravelMode(travelMode);

        if ("DRIVING".equals(mode)) {
            return "EASY";
        }

        if ("BICYCLING".equals(mode)) {
            if (totalDistanceMeters < 8_000 && totalElevationGain < 120) return "EASY";
            if (totalDistanceMeters < 20_000 && totalElevationGain < 350) return "MODERATE";
            return "HARD";
        }

        if (totalDistanceMeters < 4_000 && totalElevationGain < 80) return "EASY";
        if (totalDistanceMeters < 12_000 && totalElevationGain < 250) return "MODERATE";
        return "HARD";
    }

    private String normalizeTravelMode(String travelMode) {
        if (travelMode == null || travelMode.isBlank()) {
            return "DRIVING";
        }

        return travelMode.trim().toUpperCase(Locale.ROOT);
    }

    private String encodePolyline(List<LocationDTO> waypoints) {
        StringBuilder polyline = new StringBuilder();
        long previousLat = 0L;
        long previousLng = 0L;
        boolean started = false;

        for (LocationDTO waypoint : waypoints) {
            if (waypoint.getLat() == null || waypoint.getLng() == null) {
                continue;
            }

            long lat = Math.round(waypoint.getLat() * 100_000d);
            long lng = Math.round(waypoint.getLng() * 100_000d);

            if (!started) {
                appendEncodedValue(lat, polyline);
                appendEncodedValue(lng, polyline);
                started = true;
            } else {
                appendEncodedValue(lat - previousLat, polyline);
                appendEncodedValue(lng - previousLng, polyline);
            }

            previousLat = lat;
            previousLng = lng;
        }

        return polyline.toString();
    }

    private void appendEncodedValue(long value, StringBuilder polyline) {
        long shifted = value << 1;
        if (value < 0) {
            shifted = ~shifted;
        }

        while (shifted >= 0x20) {
            polyline.append((char) ((0x20 | (shifted & 0x1f)) + 63));
            shifted >>= 5;
        }

        polyline.append((char) (shifted + 63));
    }

    private RouteResponseDTO emptyRoute() {
        return RouteResponseDTO.builder()
                .distanceMeters(0)
                .durationSeconds(0)
                .difficulty("EASY")
                .estimatedCalories(0)
                .optimizedWaypoints(List.of())
                .polyline("")
                .build();
    }
}
