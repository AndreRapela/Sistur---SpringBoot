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
    BigDecimal latitude,
    BigDecimal longitude
) {}
