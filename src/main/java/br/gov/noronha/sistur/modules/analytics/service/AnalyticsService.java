package br.gov.noronha.sistur.modules.analytics.service;

import br.gov.noronha.sistur.dto.AdminAnalyticsDTO;
import br.gov.noronha.sistur.dto.AdminStatsDTO;
import br.gov.noronha.sistur.dto.EstablishmentStatsDTO;
import br.gov.noronha.sistur.dto.TrackEventRequest;
import br.gov.noronha.sistur.modules.analytics.model.AccessLog;
import br.gov.noronha.sistur.modules.analytics.repository.AccessLogRepository;
import br.gov.noronha.sistur.modules.tourism.model.Event;
import br.gov.noronha.sistur.modules.tourism.model.Establishment;
import br.gov.noronha.sistur.modules.tourism.model.EstablishmentType;
import br.gov.noronha.sistur.modules.tourism.model.Tour;
import br.gov.noronha.sistur.modules.tourism.model.TouristPoint;
import br.gov.noronha.sistur.modules.tourism.repository.EventRepository;
import br.gov.noronha.sistur.modules.tourism.repository.EstablishmentRepository;
import br.gov.noronha.sistur.modules.tourism.repository.TourRepository;
import br.gov.noronha.sistur.modules.tourism.repository.TouristPointRepository;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private static final List<String> CONVERSION_ACTIONS = List.of(
        "WHATSAPP_CLICK",
        "WEBSITE_CLICK",
        "BOOKING_CLICK",
        "MAP_CLICK",
        "GOOGLE_SERVICE_CLICK",
        "GOOGLE_CATEGORY_CLICK",
        "ITINERARY_ADD",
        "REGISTER_SUCCESS"
    );
    private static final List<String> BUSINESS_ACTIONS = List.of(
        "WHATSAPP_CLICK",
        "WEBSITE_CLICK",
        "BOOKING_CLICK",
        "MAP_CLICK",
        "GOOGLE_SERVICE_CLICK",
        "ITINERARY_ADD"
    );
    private static final Set<String> DEMAND_ACTIONS = Set.of(
        "VIEW",
        "DETAIL_OPEN",
        "CATEGORY_FILTER",
        "SEARCH",
        "MAP_CLICK",
        "GOOGLE_SERVICE_CLICK",
        "GOOGLE_CATEGORY_CLICK",
        "ITINERARY_ADD"
    );
    private static final List<String> CATEGORY_ORDER = List.of(
        "RESTAURANT",
        "TOUR",
        "BEACH",
        "POINT",
        "CONVENIENCE",
        "HOTEL",
        "EVENT"
    );
    private static final Map<String, String> CATEGORY_LABELS = Map.of(
        "RESTAURANT", "Restaurantes",
        "TOUR", "Passeios",
        "BEACH", "Praias",
        "POINT", "Pontos turisticos",
        "CONVENIENCE", "Conveniencias",
        "HOTEL", "Hoteis",
        "EVENT", "Eventos"
    );

    private final AccessLogRepository accessLogRepository;
    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;
    private final TourRepository tourRepository;
    private final TouristPointRepository touristPointRepository;
    private final EventRepository eventRepository;

    public void recordEvent(TrackEventRequest request, Authentication authentication, String ipAddress) {
        if (request == null || request.targetType() == null || request.targetType().isBlank()) {
            return;
        }

        accessLogRepository.save(AccessLog.builder()
            .userId(resolveUserId(authentication))
            .targetType(request.targetType())
            .targetId(resolveTargetId(request.targetId()))
            .targetLabel(normalizeLabel(request.targetLabel()))
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

    public AdminAnalyticsDTO getAdminAnalytics() {
        LocalDate startDate = LocalDate.now().minusDays(29);
        LocalDateTime since = startDate.atStartOfDay();
        List<AccessLog> recentLogs = accessLogRepository.findByTimestampAfterOrderByTimestampAsc(since);

        Map<Long, Establishment> establishments = establishmentRepository.findAll().stream()
            .collect(Collectors.toMap(Establishment::getId, item -> item));
        Map<Long, Tour> tours = tourRepository.findAll().stream()
            .collect(Collectors.toMap(Tour::getId, item -> item));
        Map<Long, TouristPoint> touristPoints = touristPointRepository.findAll().stream()
            .collect(Collectors.toMap(TouristPoint::getId, item -> item));
        Map<Long, Event> events = eventRepository.findAll().stream()
            .collect(Collectors.toMap(Event::getId, item -> item));

        long totalUsers = userRepository.count();
        long active30 = accessLogRepository.countActiveUsers(since);
        long registrationsLast30Days = countAction(recentLogs, "REGISTER_SUCCESS");
        long totalRequests = accessLogRepository.count();
        long totalConversions = accessLogRepository.countByActionTypeIn(CONVERSION_ACTIONS);
        long requestsLast30Days = recentLogs.size();
        long conversionsLast30Days = countActions(recentLogs, CONVERSION_ACTIONS);
        long googleServiceClicks = countAction(recentLogs, "GOOGLE_SERVICE_CLICK");
        long googleCategoryClicks = countAction(recentLogs, "GOOGLE_CATEGORY_CLICK");
        long googleClicks = googleServiceClicks + googleCategoryClicks;
        long itineraryAdds = countAction(recentLogs, "ITINERARY_ADD");
        long detailOpens = countAction(recentLogs, "DETAIL_OPEN");
        long pageViews = countAction(recentLogs, "VIEW");
        long searchEvents = countAction(recentLogs, "SEARCH");

        Map<String, Long> categoryDemand = initCategoryMap();
        Map<String, Long> conversionByCategory = initCategoryMap();

        recentLogs.forEach(log -> {
            String action = normalizeActionType(log.getActionType());
            String category = classifyCategory(log, establishments, touristPoints);

            if (category != null && DEMAND_ACTIONS.contains(action)) {
                categoryDemand.merge(category, 1L, Long::sum);
            }

            if (category != null && CONVERSION_ACTIONS.contains(action)) {
                conversionByCategory.merge(category, 1L, Long::sum);
            }
        });

        Map<String, Long> funnel = new LinkedHashMap<>();
        funnel.put("Visualizacoes", pageViews);
        funnel.put("Aberturas de detalhe", detailOpens);
        funnel.put("Salvos no roteiro", itineraryAdds);
        funnel.put("Cliques Google", googleClicks);

        double conversionRate = rate(conversionsLast30Days, requestsLast30Days);
        double googleConversionRate = rate(googleClicks, requestsLast30Days);

        return new AdminAnalyticsDTO(
            totalUsers,
            active30,
            registrationsLast30Days,
            totalRequests,
            totalConversions,
            requestsLast30Days,
            conversionsLast30Days,
            googleServiceClicks,
            googleCategoryClicks,
            itineraryAdds,
            detailOpens,
            pageViews,
            searchEvents,
            conversionRate,
            googleConversionRate,
            toCategoryEntries(categoryDemand),
            toCategoryEntries(conversionByCategory),
            topEntries(recentLogs, log -> isAction(log, "GOOGLE_SERVICE_CLICK") || isAction(log, "GOOGLE_CATEGORY_CLICK"), establishments, tours, touristPoints, events),
            topEntries(recentLogs, log -> isAction(log, "VIEW") && log.getTargetId() != null, establishments, tours, touristPoints, events),
            dailyMetrics(recentLogs, startDate, log -> isAction(log, "REGISTER_SUCCESS")),
            dailyMetrics(recentLogs, startDate, log -> isAction(log, "GOOGLE_SERVICE_CLICK") || isAction(log, "GOOGLE_CATEGORY_CLICK")),
            dailyMetrics(recentLogs, startDate, log -> true),
            funnel
        );
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

        long conversions = accessLogRepository.countByTargetTypeAndTargetIdAndActionTypeIn("ESTABLISHMENT", establishmentId, BUSINESS_ACTIONS);
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

    private long countAction(List<AccessLog> logs, String actionType) {
        return logs.stream()
            .filter(log -> isAction(log, actionType))
            .count();
    }

    private long countActions(List<AccessLog> logs, Collection<String> actionTypes) {
        Set<String> normalizedActions = actionTypes.stream()
            .map(this::normalizeActionType)
            .collect(Collectors.toSet());

        return logs.stream()
            .filter(log -> normalizedActions.contains(normalizeActionType(log.getActionType())))
            .count();
    }

    private boolean isAction(AccessLog log, String actionType) {
        return normalizeActionType(log.getActionType()).equals(normalizeActionType(actionType));
    }

    private Map<String, Long> initCategoryMap() {
        Map<String, Long> categories = new LinkedHashMap<>();
        CATEGORY_ORDER.forEach(category -> categories.put(category, 0L));
        return categories;
    }

    private List<AdminAnalyticsDTO.MetricEntryDTO> toCategoryEntries(Map<String, Long> counts) {
        long total = counts.values().stream().mapToLong(Long::longValue).sum();
        List<AdminAnalyticsDTO.MetricEntryDTO> entries = new ArrayList<>();

        CATEGORY_ORDER.forEach(category -> entries.add(new AdminAnalyticsDTO.MetricEntryDTO(
            category,
            CATEGORY_LABELS.getOrDefault(category, category),
            counts.getOrDefault(category, 0L),
            rate(counts.getOrDefault(category, 0L), total)
        )));

        counts.keySet().stream()
            .filter(category -> !CATEGORY_ORDER.contains(category))
            .sorted()
            .forEach(category -> entries.add(new AdminAnalyticsDTO.MetricEntryDTO(
                category,
                CATEGORY_LABELS.getOrDefault(category, category),
                counts.getOrDefault(category, 0L),
                rate(counts.getOrDefault(category, 0L), total)
            )));

        return entries;
    }

    private List<AdminAnalyticsDTO.MetricEntryDTO> topEntries(
        List<AccessLog> logs,
        Predicate<AccessLog> filter,
        Map<Long, Establishment> establishments,
        Map<Long, Tour> tours,
        Map<Long, TouristPoint> touristPoints,
        Map<Long, Event> events
    ) {
        Map<String, Long> counts = new HashMap<>();

        logs.stream()
            .filter(filter)
            .forEach(log -> counts.merge(labelForLog(log, establishments, tours, touristPoints, events), 1L, Long::sum));

        long total = counts.values().stream().mapToLong(Long::longValue).sum();

        return counts.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
            .limit(8)
            .map(entry -> new AdminAnalyticsDTO.MetricEntryDTO(entry.getKey(), entry.getKey(), entry.getValue(), rate(entry.getValue(), total)))
            .toList();
    }

    private List<AdminAnalyticsDTO.DailyMetricDTO> dailyMetrics(
        List<AccessLog> logs,
        LocalDate startDate,
        Predicate<AccessLog> filter
    ) {
        Map<LocalDate, Long> days = new LinkedHashMap<>();
        for (int day = 0; day < 30; day++) {
            days.put(startDate.plusDays(day), 0L);
        }

        logs.stream()
            .filter(filter)
            .map(log -> log.getTimestamp().toLocalDate())
            .filter(days::containsKey)
            .forEach(date -> days.merge(date, 1L, Long::sum));

        return days.entrySet().stream()
            .map(entry -> new AdminAnalyticsDTO.DailyMetricDTO(entry.getKey().toString(), entry.getValue()))
            .toList();
    }

    private String labelForLog(
        AccessLog log,
        Map<Long, Establishment> establishments,
        Map<Long, Tour> tours,
        Map<Long, TouristPoint> touristPoints,
        Map<Long, Event> events
    ) {
        if (log.getTargetLabel() != null && !log.getTargetLabel().isBlank()) {
            return log.getTargetLabel();
        }

        Long targetId = log.getTargetId();
        String targetType = normalizeType(log.getTargetType());

        if (targetId != null) {
            if ("ESTABLISHMENT".equals(targetType) && establishments.containsKey(targetId)) {
                return establishments.get(targetId).getName();
            }
            if ("TOUR".equals(targetType) && tours.containsKey(targetId)) {
                return tours.get(targetId).getName();
            }
            if ("TOURIST_POINT".equals(targetType) && touristPoints.containsKey(targetId)) {
                return touristPoints.get(targetId).getName();
            }
            if ("EVENT".equals(targetType) && events.containsKey(targetId)) {
                return events.get(targetId).getTitle();
            }
        }

        String category = classifyCategory(log, establishments, touristPoints);
        if (category != null) {
            return CATEGORY_LABELS.getOrDefault(category, category);
        }

        return targetType == null || targetType.isBlank() ? "Sem identificacao" : targetType;
    }

    private String classifyCategory(
        AccessLog log,
        Map<Long, Establishment> establishments,
        Map<Long, TouristPoint> touristPoints
    ) {
        String targetType = normalizeType(log.getTargetType());

        if (targetType.startsWith("CATEGORY_")) {
            return normalizeCategoryKey(targetType.substring("CATEGORY_".length()));
        }

        if (targetType.startsWith("GOOGLE_")) {
            return normalizeCategoryKey(targetType.substring("GOOGLE_".length()));
        }

        String directCategory = normalizeCategoryKey(targetType);
        if (directCategory != null) {
            return directCategory;
        }

        if ("ESTABLISHMENT".equals(targetType)) {
            Establishment establishment = log.getTargetId() == null ? null : establishments.get(log.getTargetId());
            if (establishment != null) {
                return categoryForEstablishmentType(establishment.getType());
            }
        }

        if ("TOUR".equals(targetType)) {
            return "TOUR";
        }

        if ("TOURIST_POINT".equals(targetType)) {
            TouristPoint touristPoint = log.getTargetId() == null ? null : touristPoints.get(log.getTargetId());
            return categoryForTouristPoint(touristPoint);
        }

        if ("EVENT".equals(targetType)) {
            return "EVENT";
        }

        return categoryFromPath(log.getPagePath());
    }

    private String categoryFromPath(String pagePath) {
        if (pagePath == null || pagePath.isBlank()) {
            return null;
        }

        String path = pagePath.toLowerCase(Locale.ROOT);
        if (path.contains("category=restaurant") || path.contains("type=restaurant") || path.contains("/restaurants") || path.contains("gastronomia")) {
            return "RESTAURANT";
        }
        if (path.contains("category=hotel") || path.contains("type=hotel") || path.contains("/hotels") || path.contains("hospedagem")) {
            return "HOTEL";
        }
        if (path.contains("category=tour") || path.contains("type=tour") || path.contains("/tours") || path.contains("passeio")) {
            return "TOUR";
        }
        if (path.contains("category=convenience") || path.contains("conveniencia") || path.contains("conveni%C3%AAncia".toLowerCase(Locale.ROOT))) {
            return "CONVENIENCE";
        }
        if (path.contains("category=event") || path.contains("/events") || path.contains("evento")) {
            return "EVENT";
        }
        if (path.contains("praia")) {
            return "BEACH";
        }
        if (path.contains("category=point") || path.contains("type=point") || path.contains("pontos-turisticos") || path.contains("turistico")) {
            return "POINT";
        }

        return null;
    }

    private String normalizeCategoryKey(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = value.trim().toUpperCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        if (Set.of("RESTAURANT", "BAR", "GASTRONOMIA", "FOOD").contains(normalized)) {
            return "RESTAURANT";
        }
        if (Set.of("HOTEL", "POUSADA", "RESORT", "HOSTEL", "LODGING", "HOSPEDAGEM").contains(normalized)) {
            return "HOTEL";
        }
        if (Set.of("TOUR", "TOURS", "PASSEIO", "PASSEIOS", "EXPERIENCE").contains(normalized)) {
            return "TOUR";
        }
        if (Set.of("BEACH", "PRAIA", "PRAIAS").contains(normalized)) {
            return "BEACH";
        }
        if (Set.of("POINT", "TOURIST_POINT", "PONTOS_TURISTICOS", "TURISTICOS", "HIGHLIGHT").contains(normalized)) {
            return "POINT";
        }
        if (Set.of("CONVENIENCE", "CONVENIENCIA", "CONVENIENCIAS", "GAS_STATION", "MARKET", "FAIR", "PHARMACY", "POSTO", "MERCADO", "FEIRA", "FARMACIA", "FARMACIAS").contains(normalized)) {
            return "CONVENIENCE";
        }
        if (Set.of("EVENT", "EVENTS", "EVENTO", "EVENTOS").contains(normalized)) {
            return "EVENT";
        }

        return null;
    }

    private String categoryForEstablishmentType(EstablishmentType type) {
        if (type == null) {
            return null;
        }

        return switch (type) {
            case RESTAURANT, BAR -> "RESTAURANT";
            case HOTEL, POUSADA, RESORT -> "HOTEL";
            case CONVENIENCE, GAS_STATION, MARKET, FAIR, PHARMACY -> "CONVENIENCE";
        };
    }

    private String categoryForTouristPoint(TouristPoint touristPoint) {
        if (touristPoint == null || touristPoint.getCategory() == null) {
            return "POINT";
        }

        return touristPoint.getCategory().toLowerCase(Locale.ROOT).contains("praia") ? "BEACH" : "POINT";
    }

    private String normalizeType(String targetType) {
        return targetType == null ? "" : targetType.trim().toUpperCase(Locale.ROOT);
    }

    private double rate(long value, long total) {
        if (total <= 0) {
            return 0.0;
        }

        return Math.round((value * 10000.0) / total) / 100.0;
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

    private Long resolveTargetId(Object targetId) {
        if (targetId == null) {
            return null;
        }

        if (targetId instanceof Number number) {
            return number.longValue();
        }

        String raw = String.valueOf(targetId).trim();
        if (raw.matches("-?\\d+")) {
            return Long.parseLong(raw);
        }

        return null;
    }

    private String normalizeLabel(String targetLabel) {
        if (targetLabel == null || targetLabel.isBlank()) {
            return null;
        }

        String label = targetLabel.trim();
        return label.length() > 255 ? label.substring(0, 255) : label;
    }

    private String normalizeActionType(String actionType) {
        if (actionType == null || actionType.isBlank()) {
            return "VIEW";
        }
        return actionType.trim().toUpperCase();
    }
}
