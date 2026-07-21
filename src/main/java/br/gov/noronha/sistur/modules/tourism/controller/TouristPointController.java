package br.gov.noronha.sistur.modules.tourism.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.TouristPointDTO;
import br.gov.noronha.sistur.modules.tourism.service.TouristPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tourist-points")
@RequiredArgsConstructor
public class TouristPointController {

    private final TouristPointService touristPointService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TouristPointDTO>>> getAllTouristPoints(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @PageableDefault(size = 100, sort = "name") Pageable pageable) {
        Page<TouristPointDTO> points = touristPointService.getAllTouristPoints(category, search, pageable);
        return ResponseEntity.ok(ApiResponse.success(points, "Pontos turísticos recuperados com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TouristPointDTO>> getTouristPointById(@PathVariable Long id) {
        TouristPointDTO point = touristPointService.getTouristPointById(id);
        return ResponseEntity.ok(ApiResponse.success(point, "Ponto turístico recuperado com sucesso"));
    }
}
