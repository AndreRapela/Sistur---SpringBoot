package br.gov.noronha.sistur.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

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

    private String photoUrl;

    private String contactNumber;

    private BigDecimal price;

    private String partnership; // Nome do parceiro cadastrado
    
    private BigDecimal latitude;
    private BigDecimal longitude;
}
