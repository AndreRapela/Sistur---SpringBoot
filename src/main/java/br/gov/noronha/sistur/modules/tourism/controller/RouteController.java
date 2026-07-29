package br.gov.noronha.sistur.modules.tourism.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.RouteRequestDTO;
import br.gov.noronha.sistur.dto.RouteResponseDTO;
import br.gov.noronha.sistur.modules.tourism.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<RouteResponseDTO>> calculateRoute(@Valid @RequestBody RouteRequestDTO request) {
        RouteResponseDTO response = routeService.calculateRoute(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Rota calculada com sucesso"));
    }
}
