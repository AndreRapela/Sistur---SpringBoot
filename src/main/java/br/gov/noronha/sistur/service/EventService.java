package br.gov.noronha.sistur.service;

import br.gov.noronha.sistur.dto.EventDTO;
import br.gov.noronha.sistur.model.Event;
import br.gov.noronha.sistur.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Page<EventDTO> getAllEvents(String category, Pageable pageable) {
        if (category != null && !category.equalsIgnoreCase("Todos")) {
            return eventRepository.findByCategory(category, pageable).map(this::toDTO);
        }
        return eventRepository.findAll(pageable).map(this::toDTO);
    }

    public Page<EventDTO> findUpcoming(Pageable pageable) {
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
    public Page<EventDTO> findByCategory(String category, Pageable pageable) {
        return eventRepository.findByCategory(category, pageable).map(this::toDTO);
    }
}
