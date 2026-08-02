package br.gov.noronha.sistur.modules.social.repository;

import br.gov.noronha.sistur.modules.social.model.Itinerary;
import br.gov.noronha.sistur.modules.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    Page<Itinerary> findByIsPublicTrue(Pageable pageable);
    List<Itinerary> findTop100ByIsPublicTrueOrderByCreatedAtDesc();
    List<Itinerary> findByUserOrderByCreatedAtDesc(User user);
    List<Itinerary> findByUserId(Long userId);
    Optional<Itinerary> findByShareToken(String shareToken);
    Optional<Itinerary> findByIdAndIsPublicTrue(Long id);
    long countByUserId(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Itinerary i set i.views = i.views + 1 where i.shareToken = :shareToken")
    int incrementViewsByShareToken(@Param("shareToken") String shareToken);
}
