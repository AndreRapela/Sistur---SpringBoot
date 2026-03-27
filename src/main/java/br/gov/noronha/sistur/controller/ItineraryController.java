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

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<org.springframework.data.domain.Page<br.gov.noronha.sistur.dto.ItineraryFeedDTO>>> getFeed(
            org.springframework.data.domain.Pageable pageable) {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null && !"anonymousUser".equals(authentication.getName())) ? authentication.getName() : null;
        return ResponseEntity.ok(ApiResponse.success(itineraryService.getFeed(pageable, email), "Feed carregado"));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> toggleLike(@PathVariable Long id, Authentication authentication) {
        itineraryService.toggleLike(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Like alterado"));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<br.gov.noronha.sistur.dto.ItineraryCommentDTO>> addComment(
            @PathVariable Long id, @RequestBody java.util.Map<String, String> payload, Authentication authentication) {
        br.gov.noronha.sistur.model.ItineraryComment comment = itineraryService.addComment(id, authentication.getName(), payload.get("content"));
        return ResponseEntity.ok(ApiResponse.success(
                br.gov.noronha.sistur.dto.ItineraryCommentDTO.fromEntity(comment),
                "Comentário salvo"
        ));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<List<br.gov.noronha.sistur.dto.ItineraryCommentDTO>>> getComments(@PathVariable Long id) {
        List<br.gov.noronha.sistur.dto.ItineraryCommentDTO> comments = itineraryService.getComments(id).stream()
                .map(br.gov.noronha.sistur.dto.ItineraryCommentDTO::fromEntity)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(comments, "Comentários carregados"));
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
