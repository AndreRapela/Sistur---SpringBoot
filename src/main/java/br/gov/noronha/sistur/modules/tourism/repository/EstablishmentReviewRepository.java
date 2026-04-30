package br.gov.noronha.sistur.modules.tourism.repository;

import br.gov.noronha.sistur.modules.tourism.model.EstablishmentReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstablishmentReviewRepository extends JpaRepository<EstablishmentReview, Long> {
    @Query("SELECT r FROM EstablishmentReview r JOIN FETCH r.user WHERE r.establishment.id = :establishmentId ORDER BY r.createdAt DESC")
    List<EstablishmentReview> findByEstablishmentIdOrderByCreatedAtDesc(Long establishmentId);
    
    @Query("SELECT AVG(r.rating) FROM EstablishmentReview r WHERE r.establishment.id = :establishmentId")
    Double getAverageRating(Long establishmentId);
}
