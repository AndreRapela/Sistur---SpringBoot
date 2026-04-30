package br.gov.noronha.sistur.dto;

import java.util.List;

public record RouteOptimizationRequestDTO(
    List<RouteOptimizationItemDTO> items,
    String tripStartDate,
    String tripEndDate,
    String weatherCondition,
    Integer temperatureCelsius
) {}