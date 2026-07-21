package br.gov.noronha.sistur.modules.tourism.repository;

import br.gov.noronha.sistur.modules.tourism.model.Establishment;
import br.gov.noronha.sistur.modules.tourism.model.EstablishmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EstablishmentRepository extends JpaRepository<Establishment, Long>, JpaSpecificationExecutor<Establishment> {
    boolean existsByNameIgnoreCase(String name);
    Optional<Establishment> findByNameIgnoreCase(String name);
    Page<Establishment> findByType(EstablishmentType type, Pageable pageable);
    List<Establishment> findByTypeInOrderByNameAsc(Collection<EstablishmentType> types);
    Page<Establishment> findByFoodType(String foodType, Pageable pageable);
}
