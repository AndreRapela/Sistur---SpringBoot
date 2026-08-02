package br.gov.noronha.sistur.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RouteOptimizationRequestDTO(
    @NotNull @Size(min = 1, max = 100) List<@Valid RouteOptimizationItemDTO> items,
    @Pattern(regexp = "^$|\\d{4}-\\d{2}-\\d{2}$") String tripStartDate,
    @Pattern(regexp = "^$|\\d{4}-\\d{2}-\\d{2}$") String tripEndDate,
    @Size(max = 40) String weatherCondition,
    @Min(-20) @Max(60) Integer temperatureCelsius
) {}
