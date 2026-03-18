package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.model.EstablishmentType;
import java.math.BigDecimal;

public record EstablishmentDTO(
    Long id,
    String name,
    String description,
    EstablishmentType type,
    String foodType,
    BigDecimal averagePrice,
    Double rating,
    String location,
    String photoUrl,
    String instagramUrl,
    String websiteUrl,
    String openingHours,
    String amenities,
    BigDecimal latitude,
    BigDecimal longitude
) {}
