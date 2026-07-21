package br.gov.noronha.sistur.modules.tourism.repository;

import br.gov.noronha.sistur.modules.tourism.model.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Long> {
    boolean existsByNameIgnoreCase(String name);
    Optional<Tour> findByNameIgnoreCase(String name);
    Page<Tour> findByCategory(String category, Pageable pageable);
}
