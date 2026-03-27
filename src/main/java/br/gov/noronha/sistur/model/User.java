package br.gov.noronha.sistur.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String photoUrl;
    
    @Column(length = 500)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // Se for um funcionário, aponta para o CLIENT (Dono) que o criou
    private Long businessOwnerId;

    // Se for um CLIENT, aponta para o estabelecimento que ele gerencia
    private Long ownedEstablishmentId;

    @OneToMany(mappedBy = "user")
    private List<Favorite> favorites;
}
