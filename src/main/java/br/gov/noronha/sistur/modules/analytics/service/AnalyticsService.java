package br.gov.noronha.sistur.modules.analytics.service;

import br.gov.noronha.sistur.dto.AdminStatsDTO;
import br.gov.noronha.sistur.dto.EstablishmentStatsDTO;
import br.gov.noronha.sistur.dto.TrackEventRequest;
import br.gov.noronha.sistur.modules.analytics.model.AccessLog;
import br.gov.noronha.sistur.modules.analytics.repository.AccessLogRepository;
import br.gov.noronha.sistur.modules.tourism.model.Establishment;
import br.gov.noronha.sistur.modules.tourism.repository.EstablishmentRepository;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private static final List<String> CONVERSION_ACTIONS = List.of("WHATSAPP_CLICK", "WEBSITE_CLICK", "BOOKING_CLICK", "MAP_CLICK", "ITINERARY_ADD", "REGISTER_SUCCESS");
    private static final List<String> BUSINESS_ACTIONS = List.of("WHATSAPP_CLICK", "WEBSITE_CLICK", "BOOKING_CLICK", "MAP_CLICK", "ITINERARY_ADD");

    private final AccessLogRepository accessLogRepository;
    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;

    public void recordEvent(TrackEventRequest request, Authentication authentication, String ipAddress) {
        if (request == null || request.targetType() == null || request.targetType().isBlank()) {
            return;
        }

        accessLogRepository.save(AccessLog.builder()
            .userId(resolveUserId(authentication))
            .targetType(request.targetType())
            .targetId(request.targetId())
            .actionType(normalizeActionType(request.actionType()))
            .pagePath(request.pagePath())
            .referrer(request.referrer())
            .ipAddress(ipAddress)
            .timestamp(LocalDateTime.now())
            .build());
    }

    public AdminStatsDTO getGlobalStats() {
        LocalDateTime since = LocalDateTime.now().minusDays(30);
        long totalUsers = userRepository.count();
        long active30 = accessLogRepository.countActiveUsers(since);
        long registrationsLast30Days = accessLogRepository.countByActionTypeAndTimestampAfter("REGISTER_SUCCESS", since);
        long totalReqs = accessLogRepository.count();
        long totalConversions = accessLogRepository.countByActionTypeIn(CONVERSION_ACTIONS);

        Map<String, Long> accessByEst = new HashMap<>();
        Map<String, Long> conversionByEst = new HashMap<>();
        establishmentRepository.findAll().forEach(est -> {
            long views = accessLogRepository.countByTargetTypeAndTargetIdAndActionType("ESTABLISHMENT", est.getId(), "VIEW");
            long conversions = accessLogRepository.countByTargetTypeAndTargetIdAndActionTypeIn("ESTABLISHMENT", est.getId(), CONVERSION_ACTIONS);

            if (views > 0) {
                accessByEst.put(est.getName(), views);
            }

            if (conversions > 0) {
                conversionByEst.put(est.getName(), conversions);
            }
        });

        return new AdminStatsDTO(totalUsers, active30, registrationsLast30Days, totalReqs, totalConversions, accessByEst, conversionByEst);
    }

    public EstablishmentStatsDTO getEstablishmentStats(Long establishmentId) {
        Establishment establishment = establishmentRepository.findById(establishmentId)
            .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));

        long views = accessLogRepository.countByTargetTypeAndTargetIdAndActionType("ESTABLISHMENT", establishmentId, "VIEW");
        long whatsappClicks = accessLogRepository.countByTargetTypeAndTargetIdAndActionType("ESTABLISHMENT", establishmentId, "WHATSAPP_CLICK");
        long mapClicks = accessLogRepository.countByTargetTypeAndTargetIdAndActionType("ESTABLISHMENT", establishmentId, "MAP_CLICK");
        long bookingClicks = accessLogRepository.countByTargetTypeAndTargetIdAndActionType("ESTABLISHMENT", establishmentId, "BOOKING_CLICK");
        long websiteClicks = accessLogRepository.countByTargetTypeAndTargetIdAndActionType("ESTABLISHMENT", establishmentId, "WEBSITE_CLICK");
        long itineraryAdds = accessLogRepository.countByTargetTypeAndTargetIdAndActionType("ESTABLISHMENT", establishmentId, "ITINERARY_ADD");

        Map<String, Long> conversionsByAction = new LinkedHashMap<>();
        BUSINESS_ACTIONS.forEach(action -> {
            long count = accessLogRepository.countByTargetTypeAndTargetIdAndActionType("ESTABLISHMENT", establishmentId, action);
            if (count > 0) {
                conversionsByAction.put(action, count);
            }
        });

        long conversions = whatsappClicks + mapClicks + bookingClicks + websiteClicks + itineraryAdds;
        double conversionRate = views > 0 ? (conversions * 100.0 / views) : 0.0;

        return new EstablishmentStatsDTO(
            establishment.getId(),
            establishment.getName(),
            views,
            conversions,
            whatsappClicks,
            mapClicks,
            bookingClicks,
            websiteClicks,
            itineraryAdds,
            conversionsByAction,
            conversionRate
        );
    }

    private Long resolveUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof br.gov.noronha.sistur.modules.auth.model.User user && user.getId() != null) {
            return user.getId();
        }

        if (authentication.getName() != null) {
            return userRepository.findByEmail(authentication.getName())
                .map(br.gov.noronha.sistur.modules.auth.model.User::getId)
                .orElse(null);
        }

        return null;
    }

    private String normalizeActionType(String actionType) {
        if (actionType == null || actionType.isBlank()) {
            return "VIEW";
        }
        return actionType.trim().toUpperCase();
    }
}
