package br.gov.noronha.sistur.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "establishments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Establishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstablishmentType type;

    private String foodType; // For restaurants/bars

    private BigDecimal averagePrice;

    private Double rating;

    private String location;

    private String photoUrl;

    private String instagramUrl;

    private String websiteUrl;

    private String openingHours;

    private String amenities; // JSON or comma separated
    
    private BigDecimal latitude;
    private BigDecimal longitude;
}
