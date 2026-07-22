package br.gov.noronha.sistur.modules.weather.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;

public record WeatherGatewayResponse(
    JsonNode forecast,
    JsonNode marine,
    Instant fetchedAt,
    Instant expiresAt,
    boolean stale
) {
    public WeatherGatewayResponse asStale() {
        return new WeatherGatewayResponse(forecast, marine, fetchedAt, expiresAt, true);
    }
}
