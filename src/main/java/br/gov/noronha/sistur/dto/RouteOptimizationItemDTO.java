package br.gov.noronha.sistur.dto;

public record RouteOptimizationItemDTO(
    String id,
    String type,
    String name,
    String location,
    String category,
    String bestTime,
    String bestSeason,
    String idealWeather,
    Integer day,
    String time,
    String notes,
    Double latitude,
    Double longitude
) {}