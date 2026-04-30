package br.gov.noronha.sistur.modules.analytics.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_logs")
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

    @Column(nullable = false)
    private String actionType;

    private String pagePath;

    private String referrer;

    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
