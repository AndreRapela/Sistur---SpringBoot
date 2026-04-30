package br.gov.noronha.sistur.dto;

public record TrackEventRequest(
    String targetType,
    Long targetId,
    String actionType,
    String pagePath,
    String referrer
) {}