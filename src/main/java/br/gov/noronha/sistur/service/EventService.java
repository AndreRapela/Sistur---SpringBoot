package br.gov.noronha.sistur.service;

import br.gov.noronha.sistur.dto.EventDTO;
import br.gov.noronha.sistur.model.Event;
import br.gov.noronha.sistur.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;

    public Page<EventDTO> getAllEvents(String category, Pageable pageable) {
        log.info("Buscando eventos. Categoria: {}", category);
        if (category != null && !category.equalsIgnoreCase("Todos")) {
            return eventRepository.findByCategory(category, pageable).map(this::toDTO);
        }
        return eventRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<EventDTO> findUpcoming(Pageable pageable) {
        log.info("Buscando próximos eventos");
        return eventRepository.findByDateAfter(LocalDateTime.now(), pageable).map(this::toDTO);
    }

    private EventDTO toDTO(Event event) {
        return new EventDTO(
            event.getId(),
            event.getTitle(),
            event.getDescription(),
            event.getDate(),
            event.getLocation(),
            event.getPhotoUrl(),
            event.getCategory(),
            event.getExternalBookingUrl(),
            event.getContactNumber(),
            event.getLatitude(),
            event.getLongitude()
        );
    }

    @Cacheable(value = "events", key = "#id")
    public EventDTO findById(Long id) {
        log.info("Buscando evento por ID: {}", id);
        return eventRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> {
                log.error("Evento ID {} não encontrado", id);
                return new RuntimeException("Evento não encontrado");
            });
    }

    public Page<EventDTO> findByCategory(String category, Pageable pageable) {
        log.info("Buscando eventos pela categoria estrita: {}", category);
        return eventRepository.findByCategory(category, pageable).map(this::toDTO);
    }
}
