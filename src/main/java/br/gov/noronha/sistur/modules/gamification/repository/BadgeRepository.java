package br.gov.noronha.sistur.modules.gamification.repository;

import br.gov.noronha.sistur.modules.gamification.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
