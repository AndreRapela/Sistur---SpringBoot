package br.gov.noronha.sistur.dto;

import java.math.BigDecimal;

public record TouristPointDTO(
    Long id,
    String name,
    String description,
    String category,
    String location,
    String photoUrl,
    String accessType,
    Boolean requiresTicket,
    Boolean requiresGuide,
    String bestTime,
    BigDecimal latitude,
    BigDecimal longitude
) {}