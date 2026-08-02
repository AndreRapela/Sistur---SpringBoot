package br.gov.noronha.sistur.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    @Size(max = 254)
    String email,
    
    @NotBlank(message = "A senha é obrigatória")
    @Size(max = 72)
    String password
) {}
