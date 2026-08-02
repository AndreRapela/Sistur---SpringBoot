package br.gov.noronha.sistur.modules.social.service;

import br.gov.noronha.sistur.dto.ItineraryItemRequest;
import br.gov.noronha.sistur.dto.ItinerarySaveRequest;
import br.gov.noronha.sistur.exception.ResourceNotFoundException;
import br.gov.noronha.sistur.modules.auth.model.User;
import br.gov.noronha.sistur.modules.auth.model.UserRole;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import br.gov.noronha.sistur.modules.gamification.service.GamificationService;
import br.gov.noronha.sistur.modules.social.model.Itinerary;
import br.gov.noronha.sistur.modules.social.repository.ItineraryCommentRepository;
import br.gov.noronha.sistur.modules.social.repository.ItineraryLikeRepository;
import br.gov.noronha.sistur.modules.social.repository.ItineraryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItineraryServiceTest {

    private final ItineraryRepository itineraryRepository = mock(ItineraryRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final GamificationService gamificationService = mock(GamificationService.class);
    private final ItineraryLikeRepository likeRepository = mock(ItineraryLikeRepository.class);
    private final ItineraryCommentRepository commentRepository = mock(ItineraryCommentRepository.class);
    private ItineraryService service;

    @BeforeEach
    void setUp() {
        service = new ItineraryService(
            itineraryRepository,
            userRepository,
            gamificationService,
            likeRepository,
            commentRepository
        );
    }

    @Test
    void numericIdCannotBeUsedAsLegacyShareToken() {
        assertThrows(ResourceNotFoundException.class, () -> service.getSharedItinerary("42"));

        verify(itineraryRepository, never()).incrementViewsByShareToken(any());
        verify(itineraryRepository, never()).findByShareToken(any());
    }

    @Test
    void privateItineraryCannotReceivePublicComments() {
        when(userRepository.findByEmail("visitor@example.com")).thenReturn(Optional.of(user()));
        when(itineraryRepository.findByIdAndIsPublicTrue(8L)).thenReturn(Optional.empty());

        assertThrows(
            ResourceNotFoundException.class,
            () -> service.addComment(8L, "visitor@example.com", "Gostei do roteiro")
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    void savePreservesCuratedStringReferencesAndPlanningDetails() {
        User user = user();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(itineraryRepository.save(any(Itinerary.class))).thenAnswer(invocation -> {
            Itinerary itinerary = invocation.getArgument(0);
            itinerary.setId(21L);
            return itinerary;
        });

        ItineraryItemRequest item = new ItineraryItemRequest(
            "CURATED-POINT-1",
            "BEACH",
            "Baia do Sancho",
            "/assets/places/baia-do-sancho.jpg",
            "Parque Nacional Marinho",
            "Praia",
            new BigDecimal("-3.8547000"),
            new BigDecimal("-32.4406000"),
            2,
            "08:30",
            "Confirmar acesso antes de sair"
        );

        var result = service.save(new ItinerarySaveRequest("Meu Noronha", true, List.of(item)), user.getEmail());

        assertEquals(21L, result.getId());
        assertTrue(result.isPublic());
        assertEquals("CURATED-POINT-1", result.getItems().getFirst().getReferenceId());
        assertEquals("08:30", result.getItems().getFirst().getTime());
        assertEquals("Praia", result.getItems().getFirst().getCategory());
        assertEquals(new BigDecimal("-3.8547000"), result.getItems().getFirst().getLatitude());
        verify(gamificationService).checkAndAwardBadges(user.getId());
    }

    private User user() {
        return User.builder()
            .id(4L)
            .email("visitor@example.com")
            .name("Visitante")
            .password("hash")
            .role(UserRole.USER)
            .build();
    }
}
