package br.gov.noronha.sistur.repository;

import br.gov.noronha.sistur.model.ItineraryComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItineraryCommentRepository extends JpaRepository<ItineraryComment, Long> {
    List<ItineraryComment> findByItineraryIdOrderByCreatedAtDesc(Long itineraryId);
}
