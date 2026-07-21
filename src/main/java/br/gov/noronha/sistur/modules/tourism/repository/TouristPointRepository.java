package br.gov.noronha.sistur.modules.tourism.repository;

import br.gov.noronha.sistur.modules.tourism.model.TouristPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TouristPointRepository extends JpaRepository<TouristPoint, Long>, JpaSpecificationExecutor<TouristPoint> {
    boolean existsByNameIgnoreCase(String name);
    Optional<TouristPoint> findByNameIgnoreCase(String name);
}
