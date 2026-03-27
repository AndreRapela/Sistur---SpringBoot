package br.gov.noronha.sistur.repository;

import br.gov.noronha.sistur.model.Itinerary;
import br.gov.noronha.sistur.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    Page<Itinerary> findByIsPublicTrue(Pageable pageable);
    List<Itinerary> findByIsPublicTrue();
    List<Itinerary> findByUser(User user);
    List<Itinerary> findByUserId(Long userId);
    Optional<Itinerary> findByShareToken(String shareToken);
    long countByUserId(Long userId);
}
