package br.gov.noronha.sistur.dto;

import java.util.Map;

public record EstablishmentStatsDTO(
    Long establishmentId,
    String establishmentName,
    long views,
    long conversions,
    long whatsappClicks,
    long mapClicks,
    long bookingClicks,
    long websiteClicks,
    long itineraryAdds,
    Map<String, Long> conversionsByAction,
    double conversionRate
) {}