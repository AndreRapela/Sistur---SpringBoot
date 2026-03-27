package br.gov.noronha.sistur.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.BadgeDTO;
import br.gov.noronha.sistur.model.User;
import br.gov.noronha.sistur.model.UserBadge;
import br.gov.noronha.sistur.repository.UserRepository;
import br.gov.noronha.sistur.service.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gamification")
@RequiredArgsConstructor
public class GamificationController {

    private final GamificationService gamificationService;
    private final UserRepository userRepository;

    @GetMapping("/badges")
    public ResponseEntity<ApiResponse<List<BadgeDTO>>> getMyBadges(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserBadge> userBadges = gamificationService.getUserBadges(user.getId());
        List<BadgeDTO> dtoList = userBadges.stream()
                .map(BadgeDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(dtoList, "Badges loaded success"));
    }
}
