package br.gov.noronha.sistur.dto;

public record TrackEventRequest(
    String targetType,
    Object targetId,
    String targetLabel,
    String actionType,
    String pagePath,
    String referrer
) {}
