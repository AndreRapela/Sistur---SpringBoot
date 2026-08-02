package br.gov.noronha.sistur.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ItineraryCommentRequest(
    @NotBlank @Size(min = 2, max = 1000) String content
) {}
