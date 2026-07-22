package br.gov.noronha.sistur.modules.tourism.service;

import br.gov.noronha.sistur.dto.EstablishmentDTO;
import br.gov.noronha.sistur.modules.analytics.repository.AccessLogRepository;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import br.gov.noronha.sistur.modules.tourism.model.Establishment;
import br.gov.noronha.sistur.modules.tourism.model.EstablishmentType;
import br.gov.noronha.sistur.modules.tourism.repository.EstablishmentRepository;
import br.gov.noronha.sistur.modules.tourism.repository.EstablishmentReviewRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EstablishmentServiceTest {

    private final EstablishmentRepository establishmentRepository = mock(EstablishmentRepository.class);
    private final EstablishmentService service = new EstablishmentService(
        establishmentRepository,
        mock(EstablishmentReviewRepository.class),
        mock(AccessLogRepository.class),
        mock(UserRepository.class)
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
