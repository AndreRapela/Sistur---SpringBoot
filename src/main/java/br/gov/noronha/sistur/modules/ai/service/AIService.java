package br.gov.noronha.sistur.modules.ai.service;

import br.gov.noronha.sistur.dto.EventDTO;
import br.gov.noronha.sistur.dto.EstablishmentDTO;
import br.gov.noronha.sistur.dto.RecommendationResponseDTO;
import br.gov.noronha.sistur.dto.RouteOptimizationItemDTO;
import br.gov.noronha.sistur.dto.RouteOptimizationRequestDTO;
import br.gov.noronha.sistur.dto.RouteOptimizationResponseDTO;
import br.gov.noronha.sistur.dto.RouteRequestDTO;
import br.gov.noronha.sistur.dto.RouteResponseDTO;
import br.gov.noronha.sistur.dto.LocationDTO;
import br.gov.noronha.sistur.modules.tourism.model.Event;
import br.gov.noronha.sistur.modules.tourism.model.Establishment;
import br.gov.noronha.sistur.modules.tourism.model.TouristPoint;
import br.gov.noronha.sistur.modules.tourism.model.Tour;
import br.gov.noronha.sistur.modules.tourism.repository.EventRepository;
import br.gov.noronha.sistur.modules.tourism.repository.EstablishmentRepository;
import br.gov.noronha.sistur.modules.tourism.repository.TouristPointRepository;
import br.gov.noronha.sistur.modules.tourism.repository.TourRepository;
import br.gov.noronha.sistur.modules.tourism.service.RouteService;
import br.gov.noronha.sistur.modules.tourism.service.EstablishmentService;
import br.gov.noronha.sistur.modules.weather.service.WeatherGatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIService {

    private final EstablishmentService establishmentService;
    private final RouteService routeService;
    private final EventRepository eventRepository;
    private final TourRepository tourRepository;
    private final EstablishmentRepository establishmentRepository;
    private final TouristPointRepository touristPointRepository;
    private final WeatherGatewayService weatherGatewayService;

    /** Motor de recomendação baseado em contexto, horário e previsão atual da ilha. */
    public RecommendationResponseDTO getSmartRecommendation() {
        LocalTime now = LocalTime.now();
        String weather = currentWeatherCondition();
        
        // Regras de Contexto (Elite Performance Strategy)
        String type;
        String summary;
        String reasoning;

        if (weather.equals("RAINY") || weather.equals("STORM")) {
            type = "RAINY_DAY";
            summary = "Parece que vai chover em Noronha hoje!";
            reasoning = "Priorizamos locais cobertos, gastronomia interna e experiências culturais para garantir seu conforto.";
        } else if (now.isBefore(LocalTime.of(12, 0))) {
            type = "MORNING";
            summary = "Bom dia no Paraíso!";
            reasoning = "Sugestões de café da manhã e locais que abrem cedo para aproveitar o sol da manhã.";
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            type = "AFTERNOON";
            summary = "Tarde perfeita para explorar!";
            reasoning = "Focamos em paradas estratégicas para almoço e locais ideais para o pôr do sol.";
        } else {
            type = "EVENING";
            summary = "Noite agitada em Noronha!";
            reasoning = "O melhor da vida noturna, jantares sofisticados e música ao vivo.";
        }

        List<EstablishmentDTO> suggestions = getFilteredSuggestions(type);

        return new RecommendationResponseDTO(
            summary,
            weather,
            type,
            suggestions,
            reasoning
        );
    }

    public RouteOptimizationResponseDTO optimizeRoute(RouteOptimizationRequestDTO request) {
        List<RouteOptimizationItemDTO> inputItems = request != null && request.items() != null
            ? request.items()
            : List.of();

        if (inputItems.isEmpty()) {
            return new RouteOptimizationResponseDTO(
                "Adicione paradas ao roteiro para receber uma otimização.",
                "Sem itens suficientes para calcular o caminho ideal.",
                List.of("Monte pelo menos duas paradas para ver a IA em ação.", "Itens com horário fixo têm prioridade."),
                currentContextType(),
                0,
                0,
                "EASY",
                List.of()
            );
        }

        List<ResolvedRouteItem> resolved = inputItems.stream()
            .map(this::resolveItem)
            .filter(Objects::nonNull)
            .toList();

        TripContext context = resolveTripContext(request, resolved);
        List<ResolvedRouteItem> optimized = optimizeResolvedItems(resolved, context);
        List<RouteOptimizationItemDTO> optimizedItems = optimized.stream()
            .map(this::toDto)
            .toList();

        RouteRequestDTO routeRequest = new RouteRequestDTO();
        routeRequest.setTravelMode("DRIVING");
        routeRequest.setWaypoints(optimized.stream()
            .filter(ResolvedRouteItem::hasCoordinates)
            .map(item -> LocationDTO.builder()
                .lat(item.latitude())
                .lng(item.longitude())
                .name(item.name())
                .build())
            .toList());

        RouteResponseDTO routeSummary = routeService.calculateRoute(routeRequest);

        return new RouteOptimizationResponseDTO(
            buildSummary(optimized, context),
            buildReasoning(resolved, optimized, context),
            buildTips(resolved, optimized, context),
            currentContextType(),
            roundKm(routeSummary.getDistanceMeters()),
            Math.max(1, routeSummary.getDurationSeconds() / 60),
            routeSummary.getDifficulty(),
            optimizedItems
        );
    }

    private List<EstablishmentDTO> getFilteredSuggestions(String type) {
        // Heuristica deterministica usada quando nao ha um modelo externo configurado.
        return establishmentService.findAll(0, 50).getContent().stream()
                .filter(e -> {
                    if (type.equals("RAINY_DAY")) return e.type().toString().equals("RESTAURANT");
                    if (type.equals("EVENING")) return e.averagePrice() != null && e.averagePrice().doubleValue() > 80;
                    return true;
                })
                .limit(3)
                .collect(Collectors.toList());
    }

    private String currentWeatherCondition() {
        try {
            int weatherCode = weatherGatewayService.getNoronhaWeather()
                .forecast()
                .path("current")
                .path("weather_code")
                .asInt(-1);

            if (weatherCode == 0 || weatherCode == 1) return "SUNNY";
            if (weatherCode >= 95) return "STORM";
            if (weatherCode >= 51) return "RAINY";
            if (weatherCode >= 2) return "CLOUDY";
            return "UNKNOWN";
        } catch (RuntimeException exception) {
            log.warn("Clima indisponível para recomendação contextual: {}", exception.getClass().getSimpleName());
            return "UNKNOWN";
        }
    }

    private TripContext resolveTripContext(RouteOptimizationRequestDTO request, List<ResolvedRouteItem> items) {
        LocalDate startDate = parseDate(request == null ? null : request.tripStartDate());
        LocalDate endDate = parseDate(request == null ? null : request.tripEndDate());
        String weatherCondition = normalizeContextValue(request == null ? null : request.weatherCondition());
        Integer temperatureCelsius = request == null ? null : request.temperatureCelsius();

        int tripDays = resolveTripDays(items, startDate, endDate);
        return new TripContext(startDate, endDate, weatherCondition, temperatureCelsius, tripDays);
    }

    private int resolveTripDays(List<ResolvedRouteItem> items, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && !endDate.isBefore(startDate)) {
            return (int) Math.max(1, ChronoUnit.DAYS.between(startDate, endDate) + 1);
        }

        int maxExplicitDay = items.stream()
            .map(ResolvedRouteItem::day)
            .filter(day -> day != null && day > 0)
            .max(Integer::compareTo)
            .orElse(0);

        if (maxExplicitDay > 0) {
            return maxExplicitDay;
        }

        return Math.max(1, Math.min(5, (int) Math.ceil(items.size() / 3.0)));
    }

    private List<ResolvedRouteItem> optimizeResolvedItems(List<ResolvedRouteItem> items, TripContext context) {
        Map<Integer, List<ResolvedRouteItem>> byDay = new LinkedHashMap<>();
        List<ResolvedRouteItem> pending = new ArrayList<>();

        for (ResolvedRouteItem item : items) {
            int day = item.day() == null || item.day() <= 0 ? 0 : item.day();
            if (day == 0) {
                pending.add(item);
                continue;
            }

            byDay.computeIfAbsent(day, key -> new ArrayList<>()).add(item);
        }

        int maxDay = Math.max(context.tripDays(), byDay.keySet().stream().max(Integer::compareTo).orElse(1));

        if (!pending.isEmpty()) {
            List<ResolvedRouteItem> orderedPending = pending.stream()
                .sorted(Comparator.comparingInt((ResolvedRouteItem item) -> scoreItem(item, context)).reversed()
                    .thenComparing(ResolvedRouteItem::name, String.CASE_INSENSITIVE_ORDER))
                .toList();

            Map<Integer, Integer> loadByDay = new HashMap<>();
            for (int day = 1; day <= maxDay; day++) {
                loadByDay.put(day, byDay.getOrDefault(day, List.of()).size());
            }

            for (ResolvedRouteItem item : orderedPending) {
                int targetDay = selectLeastLoadedDay(loadByDay, maxDay);
                byDay.computeIfAbsent(targetDay, key -> new ArrayList<>()).add(item.withDay(targetDay));
                loadByDay.put(targetDay, loadByDay.get(targetDay) + 1);
            }
        }

        List<ResolvedRouteItem> ordered = new ArrayList<>();
        byDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> ordered.addAll(optimizeDayGroup(entry.getValue(), context)));
        return ordered;
    }

    private int selectLeastLoadedDay(Map<Integer, Integer> loadByDay, int maxDay) {
        int selectedDay = 1;
        int selectedLoad = Integer.MAX_VALUE;

        for (int day = 1; day <= maxDay; day++) {
            int load = loadByDay.getOrDefault(day, 0);
            if (load < selectedLoad) {
                selectedDay = day;
                selectedLoad = load;
            }
        }

        return selectedDay;
    }

    private List<ResolvedRouteItem> optimizeDayGroup(List<ResolvedRouteItem> items, TripContext context) {
        if (items.isEmpty()) {
            return List.of();
        }

        List<ResolvedRouteItem> scheduled = items.stream()
                .filter(item -> item.time() != null && !item.time().isBlank())
                .sorted(Comparator.comparing(ResolvedRouteItem::parsedTime))
                .toList();

        List<ResolvedRouteItem> flexible = items.stream()
                .filter(item -> item.time() == null || item.time().isBlank())
            .sorted(Comparator.comparingInt((ResolvedRouteItem item) -> scoreItem(item, context)).reversed()
                        .thenComparing(ResolvedRouteItem::name, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toCollection(ArrayList::new));

        List<ResolvedRouteItem> orderedFlexible = nearestNeighbor(flexible);

        List<ResolvedRouteItem> ordered = new ArrayList<>(scheduled);
        ordered.addAll(orderedFlexible);
        return ordered;
    }

    private List<ResolvedRouteItem> nearestNeighbor(List<ResolvedRouteItem> items) {
        if (items.size() <= 1) {
            return new ArrayList<>(items);
        }

        List<ResolvedRouteItem> withCoordinates = new ArrayList<>(items.stream()
                .filter(ResolvedRouteItem::hasCoordinates)
                .toList());
        List<ResolvedRouteItem> withoutCoordinates = new ArrayList<>(items.stream()
                .filter(item -> !item.hasCoordinates())
                .toList());

        List<ResolvedRouteItem> ordered = new ArrayList<>();
        if (!withCoordinates.isEmpty()) {
            ResolvedRouteItem current = withCoordinates.remove(0);
            ordered.add(current);

            while (!withCoordinates.isEmpty()) {
                ResolvedRouteItem next = findNearest(current, withCoordinates);
                ordered.add(next);
                withCoordinates.remove(next);
                current = next;
            }
        }

        ordered.addAll(withoutCoordinates);
        return ordered;
    }

    private ResolvedRouteItem findNearest(ResolvedRouteItem origin, List<ResolvedRouteItem> candidates) {
        return candidates.stream()
                .min(Comparator.comparingDouble(candidate -> distance(origin.latitude(), origin.longitude(), candidate.latitude(), candidate.longitude())))
                .orElse(candidates.get(0));
    }

    private ResolvedRouteItem resolveItem(RouteOptimizationItemDTO item) {
        if (item == null) {
            return null;
        }

        String type = item.type() == null ? "" : item.type().trim().toUpperCase(Locale.ROOT);
        Long id = parseLong(item.id());

        if (id == null) {
            return new ResolvedRouteItem(
                    item.id(),
                    type,
                    item.name(),
                    item.location(),
                    item.category(),
                    item.bestTime(),
                    item.bestSeason(),
                    item.idealWeather(),
                    item.day(),
                    item.time(),
                    item.notes(),
                    item.latitude(),
                    item.longitude()
            );
        }

        if ("EVENT".equals(type)) {
            return eventRepository.findById(id)
                    .map(event -> new ResolvedRouteItem(
                            item.id(),
                            type,
                            defaultString(item.name(), event.getTitle()),
                            defaultString(item.location(), event.getLocation()),
                    defaultString(item.category(), event.getCategory()),
                    item.bestTime(),
                    item.bestSeason(),
                    item.idealWeather(),
                            item.day(),
                            item.time(),
                            item.notes(),
                            toDouble(event.getLatitude()),
                            toDouble(event.getLongitude())
                    ))
                    .orElse(toResolvedFallback(item, type));
        }

        if ("TOUR".equals(type)) {
            return tourRepository.findById(id)
                    .map(tour -> new ResolvedRouteItem(
                            item.id(),
                            type,
                            defaultString(item.name(), tour.getName()),
                            defaultString(item.location(), tour.getCategory()),
                    defaultString(item.category(), tour.getCategory()),
                    item.bestTime(),
                    item.bestSeason(),
                    item.idealWeather(),
                            item.day(),
                            item.time(),
                            item.notes(),
                            toDouble(tour.getLatitude()),
                            toDouble(tour.getLongitude())
                    ))
                    .orElse(toResolvedFallback(item, type));
        }

        if ("HIGHLIGHT".equals(type) || "POINT".equals(type) || "TOURIST_POINT".equals(type)) {
            return touristPointRepository.findById(id)
                .map(point -> new ResolvedRouteItem(
                    item.id(),
                    type,
                    defaultString(item.name(), point.getName()),
                    defaultString(item.location(), point.getLocation()),
                    defaultString(item.category(), point.getCategory()),
                    defaultString(item.bestTime(), point.getBestTime()),
                    item.bestSeason(),
                    item.idealWeather(),
                    item.day(),
                    item.time(),
                    item.notes(),
                    toDouble(point.getLatitude()),
                    toDouble(point.getLongitude())
                ))
                .orElse(toResolvedFallback(item, type));
        }

        return establishmentRepository.findById(id)
                .map(establishment -> new ResolvedRouteItem(
                        item.id(),
                        type,
                        defaultString(item.name(), establishment.getName()),
                        defaultString(item.location(), establishment.getLocation()),
                    defaultString(item.category(), establishment.getType() != null ? establishment.getType().name() : null),
                item.bestTime(),
                item.bestSeason(),
                item.idealWeather(),
                        item.day(),
                        item.time(),
                        item.notes(),
                        toDouble(establishment.getLatitude()),
                        toDouble(establishment.getLongitude())
                ))
                .orElse(toResolvedFallback(item, type));
    }

    private ResolvedRouteItem toResolvedFallback(RouteOptimizationItemDTO item, String type) {
        return new ResolvedRouteItem(
                item.id(),
                type,
                item.name(),
                item.location(),
                item.category(),
                item.bestTime(),
                item.bestSeason(),
                item.idealWeather(),
                item.day(),
                item.time(),
                item.notes(),
                item.latitude(),
                item.longitude()
        );
    }

    private RouteOptimizationItemDTO toDto(ResolvedRouteItem item) {
        return new RouteOptimizationItemDTO(
                item.id(),
                item.type(),
                item.name(),
                item.location(),
            item.category(),
            item.bestTime(),
            item.bestSeason(),
            item.idealWeather(),
                item.day(),
                item.time(),
                item.notes(),
                item.latitude(),
                item.longitude()
        );
    }

        private String buildSummary(List<ResolvedRouteItem> items, TripContext context) {
        long dayCount = items.stream().map(ResolvedRouteItem::day).filter(day -> day != null && day > 0).distinct().count();
        long flexibleCount = items.stream().filter(item -> item.day() == null || item.day() <= 0).count();
        String climateSummary = context.weatherCondition() == null || context.weatherCondition().isBlank()
            ? "o clima da viagem"
            : "clima " + context.weatherCondition().toLowerCase(Locale.ROOT) + " e temperatura de " + context.temperatureCelsius() + "°C";

        return "Roteiro otimizado com " + items.size() + " paradas, distribuídas em " + Math.max(1, dayCount) + " dia(s), considerando " + climateSummary + "." +
            (flexibleCount > 0 ? " Os itens pendentes foram redistribuídos para encaixar melhor no ritmo do passeio." : "");
    }

        private String buildReasoning(List<ResolvedRouteItem> original, List<ResolvedRouteItem> optimized, TripContext context) {
        long timed = original.stream().filter(item -> item.time() != null && !item.time().isBlank()).count();
        long coords = optimized.stream().filter(ResolvedRouteItem::hasCoordinates).count();
        String weather = context.weatherCondition() == null || context.weatherCondition().isBlank() ? "clima atual" : context.weatherCondition().toLowerCase(Locale.ROOT);
        String temperature = context.temperatureCelsius() == null ? "temperatura não informada" : context.temperatureCelsius() + "°C";

        return "A IA priorizou horários marcados, temperatura de " + temperature + ", o clima " + weather + " e depois agrupou as paradas por dia para reduzir deslocamentos usando " + coords +
            " pontos com coordenadas conhecidas. " +
            (timed > 0 ? "Itens com horário fixo ficaram na frente da organização." : "Como não havia horários fixos, o foco ficou na menor distância entre as paradas e na leitura melhor do dia.");
    }

        private List<String> buildTips(List<ResolvedRouteItem> original, List<ResolvedRouteItem> optimized, TripContext context) {
        List<String> tips = new ArrayList<>();
        long fixedTimeCount = original.stream().filter(item -> item.time() != null && !item.time().isBlank()).count();
        long pendingCount = original.stream().filter(item -> item.day() == null || item.day() <= 0).count();
        long restaurantCount = original.stream().filter(item -> "RESTAURANT".equalsIgnoreCase(item.type())).count();

        if (fixedTimeCount > 0) {
            tips.add("Comece pelos itens com horário marcado para evitar atrasos em cascata.");
        }

        if (restaurantCount > 1) {
            tips.add("Agrupe refeições e deslocamentos próximos para gastar menos tempo em trânsito.");
        }

        if (pendingCount > 0) {
            tips.add("Os itens pendentes ficaram no final como opções flexíveis para ajustar no dia.");
        }

        if (optimized.stream().anyMatch(ResolvedRouteItem::hasCoordinates)) {
            tips.add("Use a ordem sugerida para abrir o mapa e seguir a sequência mais curta entre os pontos.");
        }

        if (context.weatherCondition() != null && !context.weatherCondition().isBlank()) {
            if ("RAINY".equalsIgnoreCase(context.weatherCondition())) {
                tips.add("Em dias de chuva, priorize pontos históricos, culturais e de apoio antes de praias e trilhas.");
            } else if (context.temperatureCelsius() != null && context.temperatureCelsius() >= 31) {
                tips.add("Com calor acima de 31°C, abra o dia cedo nas praias e deixe o meio do dia para pausas e trechos curtos.");
            }
        }

        if (tips.isEmpty()) {
            tips.add("Roteiro organizado com base na sequência atual e nas paradas disponíveis.");
        }

        return tips.stream().distinct().limit(4).toList();
    }

    private int scoreItem(ResolvedRouteItem item, TripContext context) {
        int score = item.priority();
        score += weatherScore(item, context);
        score += timeScore(item, currentContextType());
        if (item.hasCoordinates()) {
            score += 3;
        }
        return score;
    }

    private int weatherScore(ResolvedRouteItem item, TripContext context) {
        String weather = normalizeContextValue(context.weatherCondition());
        String category = normalizeContextValue(item.category());
        int temperature = context.temperatureCelsius() == null ? 28 : context.temperatureCelsius();
        int score = 0;

        boolean beachLike = containsAny(category, "PRAIA", "MERGULHO", "SURF");
        boolean cultural = containsAny(category, "HISTORICO", "CULTURA", "EDUCACAO");
        boolean viewpoint = containsAny(category, "MIRANTE");
        boolean trail = containsAny(category, "TRILHA");

        if (containsAny(weather, "RAINY", "CHUVA", "NUBLADO")) {
            if (cultural) score += 25;
            if (trail) score += 10;
            if (beachLike) score -= 10;
        } else {
            if (beachLike) score += 20;
            if (viewpoint) score += 14;
            if (containsAny(weather, "SUNNY", "SOL", "CLARO")) {
                score += 8;
            }
        }

        if (temperature >= 31) {
            if (beachLike) score += 15;
            if (trail) score -= 5;
        }

        if (temperature <= 25) {
            if (cultural || trail) score += 10;
        }

        if (containsAny(normalizeContextValue(item.idealWeather()), weather)) {
            score += 8;
        }

        return score;
    }

    private int timeScore(ResolvedRouteItem item, String currentContext) {
        String bestTime = normalizeContextValue(item.bestTime());
        if (bestTime.isBlank()) {
            return 0;
        }

        if (bestTime.contains("MANHA") && "MORNING".equalsIgnoreCase(currentContext)) {
            return 10;
        }

        if (bestTime.contains("TARDE") && "AFTERNOON".equalsIgnoreCase(currentContext)) {
            return 8;
        }

        if (bestTime.contains("NOITE") && "EVENING".equalsIgnoreCase(currentContext)) {
            return 8;
        }

        if (bestTime.contains("PÔR DO SOL") || bestTime.contains("POR DO SOL") || bestTime.contains("SUNSET")) {
            return "AFTERNOON".equalsIgnoreCase(currentContext) || "EVENING".equalsIgnoreCase(currentContext) ? 10 : 3;
        }

        return 2;
    }

    private static boolean containsAny(String source, String... needles) {
        if (source == null || source.isBlank()) {
            return false;
        }

        for (String needle : needles) {
            if (source.contains(normalizeContextValue(needle))) {
                return true;
            }
        }

        return false;
    }

    private static String normalizeContextValue(String value) {
        if (value == null) {
            return "";
        }

        String normalized = java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "");
        return normalized.toUpperCase(Locale.ROOT);
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    private String currentContextType() {
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.of(12, 0))) {
            return "MORNING";
        }
        if (now.isBefore(LocalTime.of(18, 0))) {
            return "AFTERNOON";
        }
        return "EVENING";
    }

    private Double toDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String defaultString(String preferred, String fallback) {
        return preferred != null && !preferred.isBlank() ? preferred : fallback;
    }

    private double roundKm(long meters) {
        return Math.round((meters / 1000.0) * 100.0) / 100.0;
    }

    private double distance(Double lat1, Double lon1, Double lat2, Double lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return Double.MAX_VALUE;
        }

        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                      Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(Math.min(1, Math.max(-1, dist)));
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        return dist * 1.609344 * 1000;
    }

    private record ResolvedRouteItem(
            String id,
            String type,
            String name,
            String location,
            String category,
            String bestTime,
            String bestSeason,
            String idealWeather,
            Integer day,
            String time,
            String notes,
            Double latitude,
            Double longitude
    ) {
        ResolvedRouteItem withDay(Integer newDay) {
            return new ResolvedRouteItem(id, type, name, location, category, bestTime, bestSeason, idealWeather, newDay, time, notes, latitude, longitude);
        }

        boolean hasCoordinates() {
            return latitude != null && longitude != null;
        }

        LocalTime parsedTime() {
            if (time == null || time.isBlank()) {
                return LocalTime.MAX;
            }

            try {
                return LocalTime.parse(time);
            } catch (DateTimeParseException exception) {
                return LocalTime.MAX;
            }
        }

        int priority() {
            if ("EVENT".equalsIgnoreCase(type)) {
                return 80;
            }
            if ("TOUR".equalsIgnoreCase(type)) {
                return 70;
            }
            if ("RESTAURANT".equalsIgnoreCase(type)) {
                return 60;
            }
            if ("HOTEL".equalsIgnoreCase(type)) {
                return 50;
            }

            int score = 65;
            String normalizedCategory = normalizeContextValue(category);
            if (normalizedCategory.contains("PRAIA") || normalizedCategory.contains("MERGULHO") || normalizedCategory.contains("SURF")) {
                score += 20;
            }
            if (normalizedCategory.contains("MIRANTE")) {
                score += 14;
            }
            if (normalizedCategory.contains("HISTORICO") || normalizedCategory.contains("CULTURA") || normalizedCategory.contains("EDUCACAO")) {
                score += 10;
            }
            if (normalizedCategory.contains("TRILHA")) {
                score += 12;
            }
            return score;
        }
    }

    private record TripContext(
            LocalDate startDate,
            LocalDate endDate,
            String weatherCondition,
            Integer temperatureCelsius,
            int tripDays
    ) {}
}
