package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.modules.tourism.model.EstablishmentType;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EstablishmentDTO(
    Long id,
    @NotBlank(message = "o nome é obrigatório")
    @Size(max = 255, message = "o nome deve ter no máximo 255 caracteres")
    String name,
    @Size(max = 1000, message = "a descrição deve ter no máximo 1000 caracteres")
    String description,
    @NotNull(message = "o tipo é obrigatório")
    EstablishmentType type,
    String foodType,
    BigDecimal averagePrice,
    Double rating,
    @NotBlank(message = "a localização é obrigatória")
    @Size(max = 255, message = "a localização deve ter no máximo 255 caracteres")
    String location,
    @Size(max = 2000, message = "a URL da foto deve ter no máximo 2000 caracteres")
    String photoUrl,
    String instagramUrl,
    @Size(max = 1000, message = "a URL do site deve ter no máximo 1000 caracteres")
    String websiteUrl,
    @Size(max = 1000, message = "o horário deve ter no máximo 1000 caracteres")
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
