package br.gov.noronha.sistur.modules.tourism.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    private String category; // Mergulho, Trilha, Barco, etc.

    @Column(length = 2048)
    private String photoUrl;

    private String photoCredit;

    private String contactNumber;

    private BigDecimal price;

    private BigDecimal rating;

    private Integer reviewCount;

    private String reviewSource;

    @Column(length = 2048)
    private String reviewUrl;

    private String partnership; // Nome do parceiro cadastrado

    private String duration;

    private String schedule;

    private String meetingPoint;

    @Column(length = 3000)
    private String itinerary;

    @Column(length = 2000)
    private String includedItems;

    @Column(length = 2000)
    private String excludedItems;

    @Column(length = 2000)
    private String requirements;

    @Column(length = 2048)
    private String bookingUrl;

    @Column(length = 2048)
    private String googleMapsUrl;

    @Column(length = 2048)
    private String sourceUrl;

    private LocalDate dataVerifiedAt;
    
    private BigDecimal latitude;
    private BigDecimal longitude;
}
