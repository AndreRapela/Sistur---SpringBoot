package br.gov.noronha.sistur.modules.social.repository;

import br.gov.noronha.sistur.modules.social.model.ItineraryComment;
import br.gov.noronha.sistur.modules.social.repository.projection.ItineraryCountProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItineraryCommentRepository extends JpaRepository<ItineraryComment, Long> {
    List<ItineraryComment> findTop50ByItineraryIdOrderByCreatedAtDesc(Long itineraryId);
    long countByItineraryId(Long itineraryId);

    @Query("select c.itinerary.id as itineraryId, count(c) as total from ItineraryComment c where c.itinerary.id in :itineraryIds group by c.itinerary.id")
    List<ItineraryCountProjection> countCommentsByItineraryIds(@Param("itineraryIds") Collection<Long> itineraryIds);
}
