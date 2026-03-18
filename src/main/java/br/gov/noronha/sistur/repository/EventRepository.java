package br.gov.noronha.sistur.repository;

import br.gov.noronha.sistur.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByCategory(String category, Pageable pageable);
    Page<Event> findByDateAfter(LocalDateTime date, Pageable pageable);
}
