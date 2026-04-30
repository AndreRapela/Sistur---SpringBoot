package br.gov.noronha.sistur.modules.social.repository;

import br.gov.noronha.sistur.modules.social.model.ItineraryLike;
import br.gov.noronha.sistur.modules.social.repository.projection.ItineraryCountProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItineraryLikeRepository extends JpaRepository<ItineraryLike, Long> {
    Optional<ItineraryLike> findByItineraryIdAndUserId(Long itineraryId, Long userId);
    long countByItineraryId(Long itineraryId);

    @Query("select l.itinerary.id from ItineraryLike l where l.user.id = :userId and l.itinerary.id in :itineraryIds")
    List<Long> findItineraryIdsLikedByUserAndItineraryIds(@Param("userId") Long userId, @Param("itineraryIds") Collection<Long> itineraryIds);

    @Query("select l.itinerary.id as itineraryId, count(l) as total from ItineraryLike l where l.itinerary.id in :itineraryIds group by l.itinerary.id")
    List<ItineraryCountProjection> countLikesByItineraryIds(@Param("itineraryIds") Collection<Long> itineraryIds);
}
