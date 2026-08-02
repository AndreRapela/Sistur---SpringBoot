package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.modules.auth.model.User;
import br.gov.noronha.sistur.modules.auth.model.UserRole;

public record LoginResponseDTO(
    String token,
    Long id,
    String name,
    String email,
    UserRole role,
    String photoUrl,
    String bio,
    Long ownedEstablishmentId
) {
    public static LoginResponseDTO fromUser(String token, User user) {
        return new LoginResponseDTO(
            token,
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.getPhotoUrl(),
            user.getBio(),
            user.getOwnedEstablishmentId()
        );
    }
}
