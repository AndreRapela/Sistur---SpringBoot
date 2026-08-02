package br.gov.noronha.sistur.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ItinerarySaveRequest(
    @NotBlank @Size(min = 3, max = 120) String name,
    boolean isPublic,
    @Size(max = 100) List<@Valid ItineraryItemRequest> items
) {}
