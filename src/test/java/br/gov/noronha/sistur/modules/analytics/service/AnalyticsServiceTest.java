package br.gov.noronha.sistur.modules.analytics.service;

import br.gov.noronha.sistur.dto.EstablishmentStatsDTO;
import br.gov.noronha.sistur.modules.analytics.repository.AccessLogRepository;
import br.gov.noronha.sistur.modules.auth.model.AuthenticatedUserPrincipal;
import br.gov.noronha.sistur.modules.auth.model.User;
import br.gov.noronha.sistur.modules.auth.model.UserRole;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import br.gov.noronha.sistur.modules.tourism.model.Establishment;
import br.gov.noronha.sistur.modules.tourism.repository.EstablishmentRepository;
import br.gov.noronha.sistur.modules.tourism.repository.EventRepository;
import br.gov.noronha.sistur.modules.tourism.repository.TourRepository;
import br.gov.noronha.sistur.modules.tourism.repository.TouristPointRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AnalyticsServiceTest {

    private final AccessLogRepository accessLogRepository = mock(AccessLogRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final EstablishmentRepository establishmentRepository = mock(EstablishmentRepository.class);
    private final AnalyticsService service = new AnalyticsService(
        accessLogRepository,
        userRepository,
        establishmentRepository,
        mock(TourRepository.class),
        mock(TouristPointRepository.class),
        mock(EventRepository.class)
    );

    @Test
    void ownerReceivesAggregatedConversionMetrics() {
        User owner = client(12L, 5L);
        var authentication = authentication(owner);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(establishmentRepository.findById(5L)).thenReturn(Optional.of(
            Establishment.builder().id(5L).name("Restaurante parceiro").build()
        ));
        List<AccessLogRepository.ActionCount> actionCounts = List.of(
            action("VIEW", 100),
            action("WHATSAPP_CLICK", 10),
            action("MAP_CLICK", 5),
            action("DIRECTIONS_CLICK", 3),
            action("WEBSITE_CLICK", 4),
            action("MENU_CLICK", 2),
            action("BOOKING_CLICK", 6),
            action("ITINERARY_ADD", 5)
        );
        when(accessLogRepository.countActionsByTarget("ESTABLISHMENT", 5L)).thenReturn(actionCounts);

        EstablishmentStatsDTO result = service.getEstablishmentStats(5L, authentication);

        assertEquals(100, result.views());
        assertEquals(35, result.conversions());
        assertEquals(8, result.mapClicks());
        assertEquals(6, result.websiteClicks());
        assertEquals(35.0, result.conversionRate());
    }

    @Test
    void clientCannotReadAnotherEstablishmentMetrics() {
        User owner = client(12L, 5L);
        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));

        RuntimeException error = assertThrows(
            RuntimeException.class,
            () -> service.getEstablishmentStats(7L, authentication(owner))
        );

        assertEquals("Acesso negado às métricas deste estabelecimento", error.getMessage());
        verify(establishmentRepository, never()).findById(7L);
        verify(accessLogRepository, never()).countActionsByTarget("ESTABLISHMENT", 7L);
    }

    private User client(Long id, Long establishmentId) {
        return User.builder()
            .id(id)
            .email("partner@sistur.test")
            .name("Parceiro")
            .password("hash")
            .role(UserRole.CLIENT)
            .ownedEstablishmentId(establishmentId)
            .build();
    }

    private UsernamePasswordAuthenticationToken authentication(User user) {
        return new UsernamePasswordAuthenticationToken(
            new AuthenticatedUserPrincipal(user.getId(), user.getEmail()),
            null,
            List.of()
        );
    }

    private AccessLogRepository.ActionCount action(String type, long total) {
        AccessLogRepository.ActionCount count = mock(AccessLogRepository.ActionCount.class);
        when(count.getActionType()).thenReturn(type);
        when(count.getTotal()).thenReturn(total);
        return count;
    }
}
