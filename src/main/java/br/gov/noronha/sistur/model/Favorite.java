package br.gov.noronha.sistur.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long entityId; // ID of the favorited item

    @Enumerated(EnumType.STRING)
    private FavoriteType type; // EVENT, TOUR, ESTABLISHMENT
}
