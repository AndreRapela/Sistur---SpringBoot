package br.gov.noronha.sistur.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.model.Itinerary;
import br.gov.noronha.sistur.service.ItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ItineraryController {

    private final ItineraryService itineraryService;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Itinerary>>> getMyItineraries(Authentication authentication) {
        List<Itinerary> itineraries = itineraryService.getMyItineraries(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(itineraries, "Roteiros carregados"));
    }

    @org.springframework.cache.annotation.Cacheable("public_itineraries")
    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<Itinerary>>> getPublicItineraries() {
        List<Itinerary> itineraries = itineraryService.getPublicItineraries();
        return ResponseEntity.ok(ApiResponse.success(itineraries, "Feed social carregado"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Itinerary>> save(@RequestBody Itinerary itinerary, Authentication authentication) {
        Itinerary saved = itineraryService.save(itinerary, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(saved, "Roteiro salvo com sucesso"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Authentication authentication) {
        itineraryService.delete(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Roteiro removido"));
    }
}
