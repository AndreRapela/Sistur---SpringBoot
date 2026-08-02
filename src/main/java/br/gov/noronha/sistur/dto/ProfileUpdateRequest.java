package br.gov.noronha.sistur.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.net.URI;

public record ProfileUpdateRequest(
    @NotBlank @Size(min = 2, max = 100) String name,
    @Size(max = 500) String bio,
    @Size(max = 200_000) String photoUrl
) {
    @AssertTrue(message = "a foto deve ser HTTPS ou uma imagem local PNG, JPEG ou WebP")
    public boolean isPhotoUrlSafe() {
        if (photoUrl == null || photoUrl.isBlank()) {
            return true;
        }

        String value = photoUrl.trim();
        if (value.matches("^data:image/(png|jpeg|webp);base64,[A-Za-z0-9+/=]+$")) {
            return true;
        }

        try {
            URI uri = URI.create(value);
            return "https".equalsIgnoreCase(uri.getScheme()) && uri.getHost() != null;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }
}
