package br.gov.noronha.sistur.modules.analytics.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "access_logs",
    indexes = {
        @Index(name = "idx_access_logs_timestamp", columnList = "timestamp"),
        @Index(name = "idx_access_logs_action_timestamp", columnList = "actionType,timestamp"),
        @Index(name = "idx_access_logs_target_action", columnList = "targetType,targetId,actionType"),
        @Index(name = "idx_access_logs_user_timestamp", columnList = "userId,timestamp")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // Null for anonymous

    @Column(nullable = false)
    private String targetType; // 'ESTABLISHMENT', 'PAGE', 'EVENT'

    private Long targetId;

    private String targetLabel;

    @Column(nullable = false)
    private String actionType;

    private String pagePath;

    private String referrer;

    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
