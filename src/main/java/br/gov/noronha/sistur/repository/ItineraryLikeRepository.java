package br.gov.noronha.sistur.repository;

import br.gov.noronha.sistur.model.ItineraryLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItineraryLikeRepository extends JpaRepository<ItineraryLike, Long> {
    Optional<ItineraryLike> findByItineraryIdAndUserId(Long itineraryId, Long userId);
    long countByItineraryId(Long itineraryId);
}