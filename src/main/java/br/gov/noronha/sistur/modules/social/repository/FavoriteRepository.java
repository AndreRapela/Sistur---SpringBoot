package br.gov.noronha.sistur.modules.social.repository;

import br.gov.noronha.sistur.modules.social.model.Favorite;
import br.gov.noronha.sistur.modules.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);
    void deleteByUserIdAndEntityIdAndType(Long userId, Long entityId, String type);
}
