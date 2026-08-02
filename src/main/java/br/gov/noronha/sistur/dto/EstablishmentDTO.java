package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.modules.tourism.model.EstablishmentType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EstablishmentDTO(
    Long id,
    @NotBlank(message = "o nome e obrigatorio")
    @Size(max = 255, message = "o nome deve ter no maximo 255 caracteres")
    String name,
    @Size(max = 1000, message = "a descricao deve ter no maximo 1000 caracteres")
    String description,
    @NotNull(message = "o tipo e obrigatorio")
    EstablishmentType type,
    @Size(max = 255, message = "o tipo de cozinha deve ter no maximo 255 caracteres")
    String foodType,
    @DecimalMin(value = "0.0", message = "o preco medio nao pode ser negativo")
    @DecimalMax(value = "1000000.0", message = "o preco medio informado e invalido")
    BigDecimal averagePrice,
    @DecimalMin(value = "0.0", message = "a nota minima e 0")
    @DecimalMax(value = "5.0", message = "a nota maxima e 5")
    Double rating,
    @NotBlank(message = "a localizacao e obrigatoria")
    @Size(max = 255, message = "a localizacao deve ter no maximo 255 caracteres")
    String location,
    @Size(max = 2000, message = "a URL da foto deve ter no maximo 2000 caracteres")
    String photoUrl,
    @Size(max = 1000, message = "a URL do Instagram deve ter no maximo 1000 caracteres")
    String instagramUrl,
    @Size(max = 1000, message = "a URL do site deve ter no maximo 1000 caracteres")
    String websiteUrl,
    @Size(max = 1000, message = "o horario deve ter no maximo 1000 caracteres")
    String openingHours,
    @Size(max = 100, message = "o contato deve ter no maximo 100 caracteres")
    String contactNumber,
    @Size(max = 2000, message = "as comodidades devem ter no maximo 2000 caracteres")
    String amenities,
    @Size(max = 255, message = "o identificador Google deve ter no maximo 255 caracteres")
    String googlePlaceId,
    @Size(max = 1000, message = "a URL do Google deve ter no maximo 1000 caracteres")
    String googleMapsUrl,
    @Size(max = 1000, message = "a URL do cardapio deve ter no maximo 1000 caracteres")
    String menuUrl,
    @Size(max = 255, message = "a faixa de preco deve ter no maximo 255 caracteres")
    String priceRange,
    @Size(max = 1500, message = "os pratos populares devem ter no maximo 1500 caracteres")
    String popularDishes,
    @Size(max = 500, message = "o melhor horario deve ter no maximo 500 caracteres")
    String bestVisitTime,
    @Size(max = 500, message = "o aviso climatico deve ter no maximo 500 caracteres")
    String weatherAdvice,
    @PositiveOrZero(message = "a quantidade de avaliacoes nao pode ser negativa")
    Integer reviewCount,
    @Size(max = 1000, message = "a URL da fonte deve ter no maximo 1000 caracteres")
    String dataSourceUrl,
    LocalDate dataVerifiedAt,
    @Size(max = 255, message = "a descricao da oferta deve ter no maximo 255 caracteres")
    String discountDescription,
    @Size(max = 255, message = "o horario da oferta deve ter no maximo 255 caracteres")
    String discountHours,
    Boolean isPremiumExclusive,
    @DecimalMin(value = "-90.0", message = "a latitude e invalida")
    @DecimalMax(value = "90.0", message = "a latitude e invalida")
    BigDecimal latitude,
    @DecimalMin(value = "-180.0", message = "a longitude e invalida")
    @DecimalMax(value = "180.0", message = "a longitude e invalida")
    BigDecimal longitude
) {}
