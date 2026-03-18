package br.gov.noronha.sistur.config;

import br.gov.noronha.sistur.model.*;
import br.gov.noronha.sistur.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final EventRepository eventRepository;
    private final TourRepository tourRepository;
    private final EstablishmentRepository establishmentRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (eventRepository.count() == 0) {
            eventRepository.save(Event.builder()
                .title("Festival de Gastronomia")
                .description("O melhor da culinária noronhense com chefs convidados.")
                .date(LocalDateTime.now().plusDays(2))
                .location("Praça da Vila dos Remédios")
                .category("Cultural")
                .photoUrl("https://images.unsplash.com/photo-1504674900247-0877df9cc836")
                .latitude(new BigDecimal("-3.8415"))
                .longitude(new BigDecimal("-32.4116"))
                .build());

            eventRepository.save(Event.builder()
                .title("Circuito de Surf Noronha")
                .description("Competição profissional na Praia da Cacimba do Padre.")
                .date(LocalDateTime.now().plusDays(5))
                .location("Cacimba do Padre")
                .category("Profissional")
                .photoUrl("https://images.unsplash.com/photo-1502680390469-be75c86b636f")
                .latitude(new BigDecimal("-3.8540"))
                .longitude(new BigDecimal("-32.4435"))
                .build());
        }

        if (tourRepository.count() == 0) {
            tourRepository.save(Tour.builder()
                .name("Mergulho Batismo")
                .description("Experiência inesquecível nas águas cristalinas de Noronha.")
                .category("Mergulho")
                .price(new BigDecimal("650.00"))
                .photoUrl("https://images.unsplash.com/photo-1544551763-46a013bb70d5")
                .partnership("Noronha Divers")
                .latitude(new BigDecimal("-3.8340"))
                .longitude(new BigDecimal("-32.4080"))
                .build());
        }

        if (establishmentRepository.count() == 0) {
            establishmentRepository.save(Establishment.builder()
                .name("Varanda de Noronha")
                .description("Restaurante com vista panorâmica e pratos típicos.")
                .type(EstablishmentType.RESTAURANT)
                .foodType("Brasileira")
                .averagePrice(new BigDecimal("150.00"))
                .rating(4.8)
                .location("Vila do Trinta")
                .photoUrl("https://images.unsplash.com/photo-1517248135467-4c7edcad34c4")
                .latitude(new BigDecimal("-3.8450"))
                .longitude(new BigDecimal("-32.4150"))
                .build());
        }

        if (userRepository.count() == 0) {
            // Admin do SisTur
            userRepository.save(User.builder()
                .name("Admin SisTur")
                .email("admin@sistur.gov.br")
                .password("admin123") // Em produção, usar BCrypt
                .role(UserRole.ADMIN)
                .build());

            // Cliente (Dono de Negócio)
            userRepository.save(User.builder()
                .name("Thiago Proprietário")
                .email("thiago@varanda.com.br")
                .password("cliente123")
                .role(UserRole.CLIENT)
                .ownedEstablishmentId(1L) // Varanda de Noronha
                .build());

            // Usuário padrão (Turista)
            userRepository.save(User.builder()
                .name("Turista Noronheiro")
                .email("turista@gmail.com")
                .password("user123")
                .role(UserRole.USER)
                .build());
        }
    }
}
