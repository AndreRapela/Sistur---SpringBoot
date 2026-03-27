package br.gov.noronha.sistur.dto;

import br.gov.noronha.sistur.model.Badge;
import br.gov.noronha.sistur.model.UserBadge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BadgeDTO {
    private Long id;
    private String name;
    private String description;
    private String iconUrl;
    private LocalDateTime awardedAt;

    public static BadgeDTO fromEntity(UserBadge userBadge) {
        Badge badge = userBadge.getBadge();
        return BadgeDTO.builder()
                .id(badge.getId())
                .name(badge.getName())
                .description(badge.getDescription())
                .iconUrl(badge.getIconUrl())
                .awardedAt(userBadge.getAwardedAt())
                .build();
    }
}
