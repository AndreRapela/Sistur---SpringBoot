package br.gov.noronha.sistur.modules.social.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "itinerary_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItineraryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "itinerary_id", nullable = false)
    private Itinerary itinerary;

    @Column(nullable = false, length = 100)
    private String referenceId;

    @Column(nullable = false, length = 40)
    private String type; // EVENT, TOUR, ESTABLISHMENT

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 2000)
    private String image;

    @Column(length = 255)
    private String location;

    @Column(length = 100)
    private String category;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;
    
    private int day;

    @Column(length = 20)
    private String scheduledTime;
    
    @Column(length = 1000)
    private String notes;
}
