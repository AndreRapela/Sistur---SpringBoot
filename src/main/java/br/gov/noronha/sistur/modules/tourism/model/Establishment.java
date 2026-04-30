package br.gov.noronha.sistur.modules.tourism.model;

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

    private String contactNumber;

    private String instagramUrl;

    private String websiteUrl;

    private String openingHours;

    private String amenities; // JSON or comma separated
    
    private BigDecimal latitude;
    private BigDecimal longitude;

    // Duo Gourmet style fields
    private String discountDescription; // Ex: "Compre 1 prato principal, ganhe outro"
    private String discountHours; // Ex: "Seg-Qui: 12h-15h, 19h-22h"
    private Boolean isPremiumExclusive;
}
