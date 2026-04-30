package br.gov.noronha.sistur.modules.tourism.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tourist_points")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TouristPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1500)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String location;

    private String photoUrl;

    private String accessType;

    private Boolean requiresTicket;

    private Boolean requiresGuide;

    private String bestTime;

    private BigDecimal latitude;

    private BigDecimal longitude;
}