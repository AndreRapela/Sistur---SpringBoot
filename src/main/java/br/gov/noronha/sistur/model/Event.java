package br.gov.noronha.sistur.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    private String location;

    private String photoUrl;

    private String category; // Musical, Cultural, Profissional, Ambiental

    private String externalBookingUrl;
    
    private BigDecimal latitude;
    private BigDecimal longitude;
}
