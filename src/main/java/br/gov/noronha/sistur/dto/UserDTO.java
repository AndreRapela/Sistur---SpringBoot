package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.model.UserRole;

public record UserDTO(
    Long id,
    String name,
    String email,
    String photoUrl,
    String bio,
    UserRole role
) {}
