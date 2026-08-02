package br.gov.noronha.sistur.modules.social.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.ItineraryCommentDTO;
import br.gov.noronha.sistur.dto.ItineraryCommentRequest;
import br.gov.noronha.sistur.dto.ItineraryFeedDTO;
import br.gov.noronha.sistur.dto.ItinerarySaveRequest;
import br.gov.noronha.sistur.dto.SharedItineraryDTO;
import br.gov.noronha.sistur.modules.social.service.ItineraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/itineraries")
@RequiredArgsConstructor
public class ItineraryController {

    private final ItineraryService itineraryService;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<SharedItineraryDTO>>> getMyItineraries(Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success(
            itineraryService.getMyItineraries(authentication.getName()),
            "Roteiros carregados"
        ));
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<SharedItineraryDTO>>> getPublicItineraries() {
        return ResponseEntity.ok(ApiResponse.success(itineraryService.getPublicItineraries(), "Roteiros públicos carregados"));
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<Page<ItineraryFeedDTO>>> getFeed(Pageable pageable, Authentication authentication) {
        String email = authentication == null ? null : authentication.getName();
        return ResponseEntity.ok(ApiResponse.success(itineraryService.getFeed(pageable, email), "Feed carregado"));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<Void>> toggleLike(@PathVariable Long id, Authentication authentication) {
        itineraryService.toggleLike(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Like alterado"));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<ItineraryCommentDTO>> addComment(
        @PathVariable Long id,
        @Valid @RequestBody ItineraryCommentRequest request,
        Authentication authentication
    ) {
        ItineraryCommentDTO comment = itineraryService.addComment(id, authentication.getName(), request.content());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
            comment,
            "Comentário salvo"
        ));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<List<ItineraryCommentDTO>>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
            itineraryService.getComments(id),
            "Comentários carregados"
        ));
    }

    @GetMapping("/share/{token}")
    public ResponseEntity<ApiResponse<SharedItineraryDTO>> getSharedItinerary(@PathVariable String token) {
        return ResponseEntity.ok(ApiResponse.success(
            itineraryService.getSharedItinerary(token),
            "Roteiro compartilhado carregado"
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SharedItineraryDTO>> save(
        @Valid @RequestBody ItinerarySaveRequest request,
        Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
            itineraryService.save(request, authentication.getName()),
            "Roteiro salvo com sucesso"
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, Authentication authentication) {
        itineraryService.delete(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Roteiro removido"));
    }
}
