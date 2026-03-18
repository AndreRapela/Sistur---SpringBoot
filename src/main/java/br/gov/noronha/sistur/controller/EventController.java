package br.gov.noronha.sistur.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.EventDTO;
import br.gov.noronha.sistur.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EventDTO>>> getAllEvents(
            @RequestParam(required = false) String category,
            Pageable pageable) {
        Page<EventDTO> events = eventService.getAllEvents(category, pageable);
        return ResponseEntity.ok(ApiResponse.success(events, "Eventos recuperados com sucesso"));
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
