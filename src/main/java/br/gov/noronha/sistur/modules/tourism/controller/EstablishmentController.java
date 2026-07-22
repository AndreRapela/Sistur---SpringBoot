package br.gov.noronha.sistur.modules.tourism.controller;

import br.gov.noronha.sistur.modules.tourism.model.EstablishmentType;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.EstablishmentDTO;
import br.gov.noronha.sistur.dto.ReviewDTO;
import br.gov.noronha.sistur.modules.tourism.service.EstablishmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/establishments")
@RequiredArgsConstructor
public class EstablishmentController {

    private final EstablishmentService establishmentService;

    @GetMapping
    @Cacheable(value = "establishments", key = "#type + '-' + #category + '-' + #search + '-' + #pageable.pageNumber")
    public ResponseEntity<ApiResponse<Page<EstablishmentDTO>>> getAllEstablishments(
            @RequestParam(required = false) EstablishmentType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            Pageable pageable) {
        Page<EstablishmentDTO> ests = establishmentService.getAllEstablishments(type, category, search, pageable);
        return ResponseEntity.ok(ApiResponse.success(ests, "Estabelecimentos recuperados com sucesso"));
    }

    @GetMapping("/map")
    @Cacheable(value = "establishments", key = "'map-' + (#types == null ? 'all' : #types.toString())")
    public ResponseEntity<ApiResponse<List<EstablishmentDTO>>> getMapEstablishments(
            @RequestParam(required = false) List<EstablishmentType> types) {
        List<EstablishmentDTO> ests = establishmentService.findByTypes(types);
        return ResponseEntity.ok(ApiResponse.success(ests, "Estabelecimentos do mapa recuperados com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EstablishmentDTO>> getById(@PathVariable Long id, Authentication authentication) {
        EstablishmentDTO est = establishmentService.findById(id, authentication);
        return ResponseEntity.ok(ApiResponse.success(est, "Estabelecimento recuperado com sucesso"));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<EstablishmentDTO>> getByType(@PathVariable EstablishmentType type, Pageable pageable) {
        return ResponseEntity.ok(establishmentService.findByType(type, pageable));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ApiResponse<EstablishmentDTO>> create(@RequestBody EstablishmentDTO dto) {
        return ResponseEntity.ok(ApiResponse.success(establishmentService.save(dto), "Estabelecimento cadastrado com sucesso"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @CacheEvict(value = "establishments", allEntries = true)
    public ResponseEntity<ApiResponse<EstablishmentDTO>> update(@PathVariable Long id, @RequestBody EstablishmentDTO dto) {
        return ResponseEntity.ok(ApiResponse.success(establishmentService.update(id, dto), "Estabelecimento atualizado com sucesso"));
    }

    @PostMapping("/{id}/reviews")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> addReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDto, Authentication authentication) {
        establishmentService.addReview(id, reviewDto, authentication);
        return ResponseEntity.ok(ApiResponse.success(null, "Avaliação enviada"));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviews(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(establishmentService.getReviews(id), "Avaliações recuperadas"));
    }
}
