package br.gov.noronha.sistur.dto;

import java.util.List;

public record RecommendationResponseDTO(
    String summary,           // Texto gerado pela "IA" (Ex: "Hoje o dia está ensolarado...")
    String weatherCondition,  // SUNNY, RAINY, etc
    String recommendationType,// MORNING, AFTERNOON, EVENING, RAINY_DAY
    List<EstablishmentDTO> suggestedEstablishments,
    String aiReasoning        // Explicação do porquê dessas escolhas
) {}
