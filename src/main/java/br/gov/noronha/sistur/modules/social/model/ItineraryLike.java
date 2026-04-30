package br.gov.noronha.sistur.modules.social.model;

import br.gov.noronha.sistur.modules.auth.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "itinerary_likes", indexes = {
    @Index(name = "idx_itinerary_likes_itinerary_id", columnList = "itinerary_id"),
    @Index(name = "idx_itinerary_likes_user_id", columnList = "user_id"),
    @Index(name = "idx_itinerary_likes_itinerary_user", columnList = "itinerary_id,user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItineraryLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
