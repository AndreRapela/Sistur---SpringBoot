package br.gov.noronha.sistur.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public record EventDTO(
    Long id,
    
    @NotBlank(message = "O título é obrigatório")
    @Size(min = 3, max = 100, message = "O título deve ter entre 3 e 100 caracteres")
    String title,
    
    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 10, max = 2000, message = "A descrição deve ter entre 10 e 2000 caracteres")
    String description,
    
    @NotNull(message = "A data é obrigatória")
    @FutureOrPresent(message = "A data deve ser no futuro ou presente")
    LocalDateTime date,
    
    @NotBlank(message = "A localização é obrigatória")
    String location,
    
    @NotBlank(message = "A foto é obrigatória")
    @Pattern(regexp = "^https?://.*", message = "URL de foto inválida")
    String photoUrl,
    
    @Pattern(regexp = "Musical|Cultural|Profissional|Ambiental", message = "Categoria inválida")
    String category,
    
    String externalBookingUrl,
    
    @Pattern(regexp = "^\\d{10,15}$", message = "Telefone inválido")
    String contactNumber,
    
    @Min(-90)
    @Max(90)
    BigDecimal latitude,
    
    @Min(-180)
    @Max(180)
    BigDecimal longitude
) {}
