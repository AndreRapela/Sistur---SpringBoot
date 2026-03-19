package br.gov.noronha.sistur.repository;

import br.gov.noronha.sistur.model.Itinerary;
import br.gov.noronha.sistur.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    List<Itinerary> findByUser(User user);
    List<Itinerary> findByIsPublicTrue();
}
