package br.gov.noronha.sistur.modules.analytics.controller;

import br.gov.noronha.sistur.dto.AdminStatsDTO;
import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.EstablishmentStatsDTO;
import br.gov.noronha.sistur.modules.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminStatsDTO>> getStats() {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getGlobalStats(), "Métricas recuperadas"));
    }

    @GetMapping("/establishments/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ApiResponse<EstablishmentStatsDTO>> getEstablishmentStats(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getEstablishmentStats(id), "Métricas do estabelecimento"));
    }
}
