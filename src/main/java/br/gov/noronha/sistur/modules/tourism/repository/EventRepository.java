package br.gov.noronha.sistur.modules.tourism.repository;

import br.gov.noronha.sistur.modules.tourism.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByTitleIgnoreCase(String title);
    Optional<Event> findByTitleIgnoreCase(String title);
    Page<Event> findByCategory(String category, Pageable pageable);
    Page<Event> findByDateAfter(LocalDateTime date, Pageable pageable);
}
