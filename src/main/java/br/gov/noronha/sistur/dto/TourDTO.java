package br.gov.noronha.sistur.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TourDTO(
    Long id,
    String name,
    String description,
    String category,
    String photoUrl,
    String photoCredit,
    BigDecimal price,
    BigDecimal rating,
    Integer reviewCount,
    String reviewSource,
    String reviewUrl,
    String partnership,
    String contactNumber,
    String duration,
    String schedule,
    String meetingPoint,
    String itinerary,
    String includedItems,
    String excludedItems,
    String requirements,
    String bookingUrl,
    String googleMapsUrl,
    String sourceUrl,
    LocalDate dataVerifiedAt,
    BigDecimal latitude,
    BigDecimal longitude
) {}
