package br.gov.noronha.sistur.repository;

import br.gov.noronha.sistur.model.ItineraryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItineraryItemRepository extends JpaRepository<ItineraryItem, Long> {
}
