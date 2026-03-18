package br.gov.noronha.sistur.repository;

import br.gov.noronha.sistur.model.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {
    Page<Tour> findByCategory(String category, Pageable pageable);
}
