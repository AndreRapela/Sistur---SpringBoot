package br.gov.noronha.sistur.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ItineraryItemRequest(
    @NotBlank @Size(max = 100) String referenceId,
    @NotBlank @Pattern(regexp = "RESTAURANT|HOTEL|EVENT|TOUR|HIGHLIGHT|POINT|BEACH|CONVENIENCE") String type,
    @NotBlank @Size(max = 255) String name,
    @Size(max = 2000) String image,
    @Size(max = 255) String location,
    @Size(max = 100) String category,
    @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal latitude,
    @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal longitude,
    @Min(0) @Max(30) int day,
    @Size(max = 20) String time,
    @Size(max = 1000) String notes
) {}
