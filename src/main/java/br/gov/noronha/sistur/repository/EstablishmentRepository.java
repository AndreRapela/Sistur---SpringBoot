package br.gov.noronha.sistur.repository;

import br.gov.noronha.sistur.model.Establishment;
import br.gov.noronha.sistur.model.EstablishmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {
    Page<Establishment> findByType(EstablishmentType type, Pageable pageable);
    Page<Establishment> findByFoodType(String foodType, Pageable pageable);
}
