package br.gov.noronha.sistur.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private Long referenceId;

    @Column(nullable = false)
    private String type; // EVENT, TOUR, ESTABLISHMENT

    @Column(nullable = false)
    private String name;

    private String image;
    private String location;
    
    private int day;
    
    @Column(length = 1000)
    private String notes;
}
