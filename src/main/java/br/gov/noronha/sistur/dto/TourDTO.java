package br.gov.noronha.sistur.dto;

import java.math.BigDecimal;

public record TourDTO(
    Long id,
    String name,
    String description,
    String category,
    String photoUrl,
    BigDecimal price,
    String partnership,
    String contactNumber,
    BigDecimal latitude,
    BigDecimal longitude
) {}
