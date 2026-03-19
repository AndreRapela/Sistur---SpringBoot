package br.gov.noronha.sistur.dto;

import java.time.LocalDateTime;

public record EventDTO(
    Long id,
    String title,
    String description,
    LocalDateTime date,
    String location,
    String photoUrl,
    String category,
    String externalBookingUrl,
    String contactNumber,
    java.math.BigDecimal latitude,
    java.math.BigDecimal longitude
) {}
