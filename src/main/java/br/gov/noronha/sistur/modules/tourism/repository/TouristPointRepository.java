package br.gov.noronha.sistur.modules.tourism.repository;

import br.gov.noronha.sistur.modules.tourism.model.TouristPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TouristPointRepository extends JpaRepository<TouristPoint, Long>, JpaSpecificationExecutor<TouristPoint> {
}