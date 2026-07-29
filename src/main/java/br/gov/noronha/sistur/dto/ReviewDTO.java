package br.gov.noronha.sistur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long establishmentId;
    private String userName;
    @NotNull(message = "a nota é obrigatória")
    @Min(value = 1, message = "a nota mínima é 1")
    @Max(value = 5, message = "a nota máxima é 5")
    private Integer rating;

    @Size(max = 2000, message = "o comentário deve ter no máximo 2000 caracteres")
    private String comment;
    private LocalDateTime createdAt;
}
