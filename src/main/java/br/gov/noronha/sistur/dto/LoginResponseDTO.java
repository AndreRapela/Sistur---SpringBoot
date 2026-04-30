package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.modules.auth.model.UserRole;

public record LoginResponseDTO(String token, String name, String email, UserRole role) {}
