package br.gov.noronha.sistur.dto;

import java.util.List;

public record RouteOptimizationResponseDTO(
    String summary,
    String aiReasoning,
    List<String> tips,
    String recommendationType,
    double estimatedDistanceKm,
    long estimatedDurationMinutes,
    String difficulty,
    List<RouteOptimizationItemDTO> optimizedItems
) {}