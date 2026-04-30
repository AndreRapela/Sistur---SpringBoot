package br.gov.noronha.sistur.modules.ai.controller;

import br.gov.noronha.sistur.dto.RecommendationResponseDTO;
import br.gov.noronha.sistur.dto.RouteOptimizationRequestDTO;
import br.gov.noronha.sistur.dto.RouteOptimizationResponseDTO;
import br.gov.noronha.sistur.modules.ai.service.AIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI Recommendation", description = "Motor de inteligência artificial para roteiros")
public class AIController {

    private final AIService aiService;

    @GetMapping("/recommend")
    @Operation(summary = "Obter recomendação baseada em clima e hora", 
               description = "Retorna uma sugestão personalizada de locais baseada no contexto atual da ilha.")
    public ResponseEntity<RecommendationResponseDTO> getRecommendation() {
        return ResponseEntity.ok(aiService.getSmartRecommendation());
    }

    @PostMapping("/optimize")
    @Operation(summary = "Otimizar roteiro com base nas paradas", 
               description = "Ordena itens do roteiro por horário, proximidade e contexto da viagem.")
    public ResponseEntity<RouteOptimizationResponseDTO> optimizeRoute(@RequestBody RouteOptimizationRequestDTO request) {
        return ResponseEntity.ok(aiService.optimizeRoute(request));
    }
}
