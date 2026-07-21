package br.gov.noronha.sistur.modules.tourism.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.EventDTO;
import br.gov.noronha.sistur.modules.tourism.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EventDTO>>> getAllEvents(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Pageable pageable) {
        Page<EventDTO> events = eventService.getAllEvents(category, org.springframework.data.domain.PageRequest.of(page, size, pageable.getSort()));
        return ResponseEntity.ok(ApiResponse.success(events, "Eventos recuperados com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDTO>> getById(@PathVariable Long id, Authentication authentication) {
        EventDTO event = eventService.findById(id, authentication);
        return ResponseEntity.ok(ApiResponse.success(event, "Evento recuperado com sucesso"));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<EventDTO>> getUpcoming(Pageable pageable) {
        return ResponseEntity.ok(eventService.findUpcoming(pageable));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<EventDTO>> getByCategory(@PathVariable String category, Pageable pageable) {
        return ResponseEntity.ok(eventService.findByCategory(category, pageable));
    }
}
