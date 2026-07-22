package br.gov.noronha.sistur.modules.weather.service;

import br.gov.noronha.sistur.modules.weather.dto.WeatherGatewayResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherGatewayService {

    private static final String LATITUDE = "-3.8415";
    private static final String LONGITUDE = "-32.4116";
    private static final String TIMEZONE = "America/Noronha";

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(6))
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    @Value("${weather.open-meteo.forecast-url:https://api.open-meteo.com/v1/forecast}")
    private String forecastUrl;

    @Value("${weather.open-meteo.marine-url:https://marine-api.open-meteo.com/v1/marine}")
    private String marineUrl;

    @Value("${weather.open-meteo.api-key:}")
    private String apiKey;

    @Value("${weather.cache-minutes:10}")
    private long cacheMinutes;

    private volatile WeatherGatewayResponse cachedResponse;
    private volatile Instant expiresAt = Instant.EPOCH;

    public WeatherGatewayResponse getNoronhaWeather() {
        var now = Instant.now();
        var cached = cachedResponse;
        if (cached != null && now.isBefore(expiresAt)) {
            return cached;
        }

        synchronized (this) {
            now = Instant.now();
            cached = cachedResponse;
            if (cached != null && now.isBefore(expiresAt)) {
                return cached;
            }

            try {
                return refresh(now);
            } catch (ResponseStatusException exception) {
                if (cached != null) {
                    log.warn("Weather providers unavailable; serving the last cached forecast", exception);
                    expiresAt = now.plus(Duration.ofMinutes(2));
                    return cached.asStale();
                }
                throw exception;
            }
        }
    }

    private WeatherGatewayResponse refresh(Instant fetchedAt) {
        var forecast = fetchJson(buildForecastUri());
        JsonNode marine = null;

        try {
            marine = fetchJson(buildMarineUri());
        } catch (ResponseStatusException exception) {
            log.warn("Marine forecast unavailable; terrestrial forecast will still be served", exception);
        }

        expiresAt = fetchedAt.plus(Duration.ofMinutes(Math.max(cacheMinutes, 1)));
        cachedResponse = new WeatherGatewayResponse(forecast, marine, fetchedAt, expiresAt, false);
        return cachedResponse;
    }

    private URI buildForecastUri() {
        var builder = UriComponentsBuilder.fromUriString(forecastUrl)
            .queryParam("latitude", LATITUDE)
            .queryParam("longitude", LONGITUDE)
            .queryParam("timezone", TIMEZONE)
            .queryParam("forecast_days", 3)
            .queryParam("current", "temperature_2m,apparent_temperature,relative_humidity_2m,precipitation,rain,weather_code,cloud_cover,wind_speed_10m,wind_gusts_10m")
            .queryParam("hourly", "temperature_2m,apparent_temperature,precipitation_probability,precipitation,weather_code,wind_gusts_10m,uv_index")
            .queryParam("daily", "weather_code,temperature_2m_max,temperature_2m_min,apparent_temperature_max,uv_index_max,precipitation_sum,precipitation_probability_max,wind_gusts_10m_max");
        addApiKey(builder);
        return builder.build().encode().toUri();
    }

    private URI buildMarineUri() {
        var builder = UriComponentsBuilder.fromUriString(marineUrl)
            .queryParam("latitude", LATITUDE)
            .queryParam("longitude", LONGITUDE)
            .queryParam("timezone", TIMEZONE)
            .queryParam("forecast_days", 3)
            .queryParam("cell_selection", "sea")
            .queryParam("current", "wave_height,wave_period,swell_wave_height,sea_surface_temperature")
            .queryParam("daily", "wave_height_max,swell_wave_height_max");
        addApiKey(builder);
        return builder.build().encode().toUri();
    }

    private void addApiKey(UriComponentsBuilder builder) {
        if (StringUtils.hasText(apiKey)) {
            builder.queryParam("apikey", apiKey.trim());
        }
    }

    private JsonNode fetchJson(URI uri) {
        var request = HttpRequest.newBuilder(uri)
            .timeout(Duration.ofSeconds(10))
            .header("Accept", "application/json")
            .GET()
            .build();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Provedor climático respondeu com status " + response.statusCode()
                );
            }
            return objectMapper.readTree(response.body());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Consulta climática interrompida", exception);
        } catch (IOException | IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Não foi possível consultar a previsão climática", exception);
        }
    }
}
