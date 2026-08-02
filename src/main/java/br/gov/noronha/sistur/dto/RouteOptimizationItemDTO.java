package br.gov.noronha.sistur.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RouteOptimizationItemDTO(
    @NotBlank @Size(max = 100) String id,
    @NotBlank @Size(max = 40) String type,
    @NotBlank @Size(max = 255) String name,
    @Size(max = 255) String location,
    @Size(max = 100) String category,
    @Size(max = 100) String bestTime,
    @Size(max = 100) String bestSeason,
    @Size(max = 100) String idealWeather,
    @Min(0) @Max(30) Integer day,
    @Size(max = 20) String time,
    @Size(max = 1000) String notes,
    @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
    @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude
) {}
