package br.gov.noronha.sistur.service;

import br.gov.noronha.sistur.model.Badge;
import br.gov.noronha.sistur.model.User;
import br.gov.noronha.sistur.model.UserBadge;
import br.gov.noronha.sistur.repository.BadgeRepository;
import br.gov.noronha.sistur.repository.ItineraryRepository;
import br.gov.noronha.sistur.repository.UserBadgeRepository;
import br.gov.noronha.sistur.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GamificationService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final ItineraryRepository itineraryRepository;

    @Transactional
    public void checkAndAwardBadges(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        long itineraryCount = itineraryRepository.countByUserId(userId);

        List<Badge> allBadges = badgeRepository.findAll();

        for (Badge badge : allBadges) {
            if (itineraryCount >= badge.getCriteria()) {
                if (!userBadgeRepository.existsByUserIdAndBadgeId(userId, badge.getId())) {
                    UserBadge userBadge = UserBadge.builder()
                            .user(user)
                            .badge(badge)
                            .awardedAt(LocalDateTime.now())
                            .build();
                    userBadgeRepository.save(userBadge);
                }
            }
        }
    }

    public List<UserBadge> getUserBadges(Long userId) {
        return userBadgeRepository.findByUserId(userId);
    }
}
