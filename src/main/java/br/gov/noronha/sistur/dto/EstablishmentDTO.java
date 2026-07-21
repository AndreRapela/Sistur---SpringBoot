package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.modules.tourism.model.EstablishmentType;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    String contactNumber,
    String amenities,
    String googlePlaceId,
    String googleMapsUrl,
    String menuUrl,
    String priceRange,
    String popularDishes,
    String bestVisitTime,
    String weatherAdvice,
    Integer reviewCount,
    String dataSourceUrl,
    LocalDate dataVerifiedAt,
    String discountDescription,
    String discountHours,
    Boolean isPremiumExclusive,
    BigDecimal latitude,
    BigDecimal longitude
) {}
