package br.gov.noronha.sistur.modules.tourism.service;

import br.gov.noronha.sistur.dto.EstablishmentDTO;
import br.gov.noronha.sistur.modules.analytics.repository.AccessLogRepository;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import br.gov.noronha.sistur.modules.auth.model.AuthenticatedUserPrincipal;
import br.gov.noronha.sistur.modules.auth.model.User;
import br.gov.noronha.sistur.modules.auth.model.UserRole;
import br.gov.noronha.sistur.modules.tourism.model.Establishment;
import br.gov.noronha.sistur.modules.tourism.model.EstablishmentType;
import br.gov.noronha.sistur.modules.tourism.repository.EstablishmentRepository;
import br.gov.noronha.sistur.modules.tourism.repository.EstablishmentReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EstablishmentServiceTest {

    private final EstablishmentRepository establishmentRepository = mock(EstablishmentRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final EstablishmentService service = new EstablishmentService(
        establishmentRepository,
        mock(EstablishmentReviewRepository.class),
        mock(AccessLogRepository.class),
        userRepository
    );

    @Test
    void findByTypesHidesLegacyDuplicatesAndKeepsCanonicalRecord() {
        Establishment legacy = establishment(1L, "Varanda de Noronha");
        Establishment canonical = establishment(5L, "Varanda Noronha");
        when(establishmentRepository.findByTypeInOrderByNameAsc(List.of(EstablishmentType.RESTAURANT)))
            .thenReturn(List.of(legacy, canonical));

        List<EstablishmentDTO> result = service.findByTypes(List.of(EstablishmentType.RESTAURANT));

        assertEquals(1, result.size());
        assertEquals(5L, result.getFirst().id());
        assertEquals("Varanda Noronha", result.getFirst().name());
    }

    @Test
    void clientCannotUpdateAnotherEstablishment() {
        User owner = User.builder()
            .id(12L)
            .email("partner@sistur.test")
            .name("Parceiro")
            .password("hash")
            .role(UserRole.CLIENT)
            .ownedEstablishmentId(5L)
            .build();
        var authentication = new UsernamePasswordAuthenticationToken(
            new AuthenticatedUserPrincipal(owner.getId(), owner.getEmail()),
            null,
            List.of()
        );
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        RuntimeException error = assertThrows(
            RuntimeException.class,
            () -> service.update(7L, mock(EstablishmentDTO.class), authentication)
        );

        assertEquals("Acesso negado a este estabelecimento", error.getMessage());
        verify(establishmentRepository, never()).findById(any());
    }

    private Establishment establishment(Long id, String name) {
        return Establishment.builder()
            .id(id)
            .name(name)
            .description("Descrição editorial")
            .type(EstablishmentType.RESTAURANT)
            .location("Fernando de Noronha")
            .build();
    }
}
