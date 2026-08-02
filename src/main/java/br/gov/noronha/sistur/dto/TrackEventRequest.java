package br.gov.noronha.sistur.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TrackEventRequest(
    @NotBlank @Size(max = 64) @Pattern(regexp = "[A-Za-z0-9_-]+")
    String targetType,
    Object targetId,
    @Size(max = 255)
    String targetLabel,
    @Size(max = 64) @Pattern(regexp = "[A-Za-z0-9_-]+")
    String actionType,
    @Size(max = 512)
    String pagePath,
    @Size(max = 1024)
    String referrer
) {}
