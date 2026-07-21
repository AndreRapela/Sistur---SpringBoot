package br.gov.noronha.sistur.modules.tourism.service;

import br.gov.noronha.sistur.modules.analytics.model.AccessLog;
import br.gov.noronha.sistur.modules.analytics.repository.AccessLogRepository;
import br.gov.noronha.sistur.dto.EventDTO;
import br.gov.noronha.sistur.modules.tourism.model.Event;
import br.gov.noronha.sistur.modules.tourism.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final AccessLogRepository accessLogRepository;

    @Cacheable(value = "events", key = "#category + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
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

    public EventDTO findById(Long id, Authentication authentication) {
        log.info("Buscando evento por ID: {}", id);
        EventDTO eventDTO = eventRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> {
                log.error("Evento ID {} não encontrado", id);
                return new RuntimeException("Evento não encontrado");
            });

        accessLogRepository.save(AccessLog.builder()
            .userId(resolveUserId(authentication))
            .targetType("EVENT")
            .targetId(id)
            .actionType("VIEW")
            .timestamp(LocalDateTime.now())
            .build());

        return eventDTO;
    }

    public Page<EventDTO> findByCategory(String category, Pageable pageable) {
        log.info("Buscando eventos pela categoria estrita: {}", category);
        return eventRepository.findByCategory(category, pageable).map(this::toDTO);
    }

    private Long resolveUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof br.gov.noronha.sistur.modules.auth.model.User user && user.getId() != null) {
            return user.getId();
        }

        return null;
    }
}
