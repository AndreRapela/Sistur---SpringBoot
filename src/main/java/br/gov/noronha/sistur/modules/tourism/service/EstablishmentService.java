package br.gov.noronha.sistur.modules.tourism.service;

import br.gov.noronha.sistur.modules.analytics.model.AccessLog;
import br.gov.noronha.sistur.modules.analytics.repository.AccessLogRepository;

import br.gov.noronha.sistur.dto.EstablishmentDTO;
import br.gov.noronha.sistur.dto.ReviewDTO;
import br.gov.noronha.sistur.modules.auth.model.User;
import br.gov.noronha.sistur.modules.auth.repository.UserRepository;
import br.gov.noronha.sistur.modules.tourism.model.Establishment;
import br.gov.noronha.sistur.modules.tourism.model.EstablishmentReview;
import br.gov.noronha.sistur.modules.tourism.model.EstablishmentType;
import br.gov.noronha.sistur.modules.tourism.repository.EstablishmentRepository;
import br.gov.noronha.sistur.modules.tourism.repository.EstablishmentReviewRepository;
import br.gov.noronha.sistur.repository.specification.EstablishmentSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;
    private final EstablishmentReviewRepository reviewRepository;
    private final AccessLogRepository accessLogRepository;
    private final UserRepository userRepository;

    public Page<EstablishmentDTO> findAll(int page, int size) {
        return establishmentRepository.findAll(PageRequest.of(page, size)).map(this::toDTO);
    }

    public Page<EstablishmentDTO> getAllEstablishments(EstablishmentType type, String category, String search, Pageable pageable) {
        Specification<Establishment> spec = Specification.where(EstablishmentSpecifications.hasType(type))
                .and(EstablishmentSpecifications.hasCategory(category))
                .and(EstablishmentSpecifications.searchByNameOrDescription(search));
        
        return establishmentRepository.findAll(spec, pageable).map(this::toDTO);
    }

    private EstablishmentDTO toDTO(Establishment e) {
        return new EstablishmentDTO(
            e.getId(), e.getName(), e.getDescription(), e.getType(),
            e.getFoodType(), e.getAveragePrice(), e.getRating(),
            e.getLocation(), e.getPhotoUrl(), e.getInstagramUrl(),
            e.getWebsiteUrl(), e.getOpeningHours(), e.getContactNumber(),
            e.getAmenities(), e.getGooglePlaceId(), e.getGoogleMapsUrl(),
            e.getMenuUrl(), e.getPriceRange(), e.getPopularDishes(),
            e.getBestVisitTime(), e.getWeatherAdvice(), e.getReviewCount(),
            e.getDataSourceUrl(), e.getDataVerifiedAt(), e.getDiscountDescription(),
            e.getDiscountHours(), e.getIsPremiumExclusive(), e.getLatitude(), e.getLongitude()
        );
    }

    public EstablishmentDTO findById(Long id, Authentication authentication) {
        Establishment establishment = establishmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));
        
        // Log access
        accessLogRepository.save(AccessLog.builder()
            .userId(resolveUserId(authentication))
            .targetType("ESTABLISHMENT")
            .targetId(id)
            .actionType("VIEW")
            .timestamp(LocalDateTime.now())
            .build());
            
        return toDTO(establishment);
    }

    @Transactional
    @CacheEvict(value = "establishments", allEntries = true)
    public void addReview(Long establishmentId, ReviewDTO reviewDTO, Authentication authentication) {
        Establishment est = establishmentRepository.findById(establishmentId)
            .orElseThrow(() -> new RuntimeException("Not found"));

        Long userId = resolveRequiredUserId(authentication);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        EstablishmentReview review = EstablishmentReview.builder()
            .establishment(est)
            .user(user)
            .rating(reviewDTO.getRating())
            .comment(reviewDTO.getComment())
            .createdAt(LocalDateTime.now())
            .build();
        
        reviewRepository.save(review);
        
        // Update average rating
        Double avg = reviewRepository.getAverageRating(establishmentId);
        est.setRating(avg);
        establishmentRepository.save(est);
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviews(Long establishmentId) {
        return reviewRepository.findByEstablishmentIdOrderByCreatedAtDesc(establishmentId)
            .stream().map(r -> new ReviewDTO(r.getId(), establishmentId, r.getUser() != null ? r.getUser().getName() : "Usuário", r.getRating(), r.getComment(), r.getCreatedAt()))
            .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "establishments", allEntries = true)
    public EstablishmentDTO save(EstablishmentDTO dto) {
        Establishment e = fromDTO(dto);
        return toDTO(establishmentRepository.save(e));
    }

    @Transactional
    public EstablishmentDTO update(Long id, EstablishmentDTO dto) {
        Establishment e = establishmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Não encontrado"));
        updateFromDTO(e, dto);
        return toDTO(establishmentRepository.save(e));
    }

    private Establishment fromDTO(EstablishmentDTO d) {
        return Establishment.builder()
            .name(d.name())
            .description(d.description())
            .type(d.type())
            .foodType(d.foodType())
            .averagePrice(d.averagePrice())
            .location(d.location())
            .photoUrl(d.photoUrl())
            .instagramUrl(d.instagramUrl())
            .websiteUrl(d.websiteUrl())
            .openingHours(d.openingHours())
            .contactNumber(d.contactNumber())
            .amenities(d.amenities())
            .googlePlaceId(d.googlePlaceId())
            .googleMapsUrl(d.googleMapsUrl())
            .menuUrl(d.menuUrl())
            .priceRange(d.priceRange())
            .popularDishes(d.popularDishes())
            .bestVisitTime(d.bestVisitTime())
            .weatherAdvice(d.weatherAdvice())
            .reviewCount(d.reviewCount())
            .dataSourceUrl(d.dataSourceUrl())
            .dataVerifiedAt(d.dataVerifiedAt())
            .discountDescription(d.discountDescription())
            .discountHours(d.discountHours())
            .isPremiumExclusive(d.isPremiumExclusive())
            .latitude(d.latitude())
            .longitude(d.longitude())
            .build();
    }

    private void updateFromDTO(Establishment e, EstablishmentDTO d) {
        e.setName(d.name());
        e.setDescription(d.description());
        e.setType(d.type());
        e.setFoodType(d.foodType());
        e.setAveragePrice(d.averagePrice());
        e.setLocation(d.location());
        e.setPhotoUrl(d.photoUrl());
        e.setInstagramUrl(d.instagramUrl());
        e.setWebsiteUrl(d.websiteUrl());
        e.setOpeningHours(d.openingHours());
        e.setContactNumber(d.contactNumber());
        e.setAmenities(d.amenities());
        e.setGooglePlaceId(d.googlePlaceId());
        e.setGoogleMapsUrl(d.googleMapsUrl());
        e.setMenuUrl(d.menuUrl());
        e.setPriceRange(d.priceRange());
        e.setPopularDishes(d.popularDishes());
        e.setBestVisitTime(d.bestVisitTime());
        e.setWeatherAdvice(d.weatherAdvice());
        e.setReviewCount(d.reviewCount());
        e.setDataSourceUrl(d.dataSourceUrl());
        e.setDataVerifiedAt(d.dataVerifiedAt());
        e.setDiscountDescription(d.discountDescription());
        e.setDiscountHours(d.discountHours());
        e.setIsPremiumExclusive(d.isPremiumExclusive());
        e.setLatitude(d.latitude());
        e.setLongitude(d.longitude());
    }

    public Page<EstablishmentDTO> findByType(EstablishmentType type, Pageable pageable) {
        return establishmentRepository.findByType(type, pageable).map(this::toDTO);
    }

    public List<EstablishmentDTO> findByTypes(List<EstablishmentType> types) {
        List<EstablishmentType> typesToLoad = (types == null || types.isEmpty())
            ? Arrays.asList(EstablishmentType.values())
            : types;

        return establishmentRepository.findByTypeInOrderByNameAsc(typesToLoad)
            .stream()
            .collect(Collectors.toMap(
                establishment -> canonicalPublicName(establishment.getName()).toLowerCase(Locale.ROOT),
                establishment -> establishment,
                this::preferredPublicRecord,
                LinkedHashMap::new
            ))
            .values()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    private Establishment preferredPublicRecord(Establishment first, Establishment second) {
        boolean firstUsesCanonicalName = first.getName().equalsIgnoreCase(canonicalPublicName(first.getName()));
        boolean secondUsesCanonicalName = second.getName().equalsIgnoreCase(canonicalPublicName(second.getName()));
        if (firstUsesCanonicalName != secondUsesCanonicalName) {
            return secondUsesCanonicalName ? second : first;
        }
        return completenessScore(second) > completenessScore(first) ? second : first;
    }

    private int completenessScore(Establishment establishment) {
        return Stream.of(
            establishment.getDescription(),
            establishment.getOpeningHours(),
            establishment.getPriceRange(),
            establishment.getPopularDishes(),
            establishment.getWebsiteUrl(),
            establishment.getContactNumber(),
            establishment.getDataSourceUrl()
        ).mapToInt(value -> value == null || value.isBlank() ? 0 : 1).sum();
    }

    private String canonicalPublicName(String name) {
        if (name == null) return "";
        Map<String, String> aliases = Map.of(
            "varanda de noronha", "Varanda Noronha",
            "farmácia noronha", "Farmácia Fernando de Noronha",
            "farmácia ilha farma", "Farmácia Nativa",
            "feirinha da vila dos remédios", "Feira Orgânica de Noronha",
            "banco 24 horas noronha", "Banco24Horas - Aeroporto de Noronha"
        );
        return aliases.getOrDefault(name.trim().toLowerCase(Locale.ROOT), name.trim());
    }

    private Long resolveUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof br.gov.noronha.sistur.modules.auth.model.User user && user.getId() != null) {
            return user.getId();
        }

        return null;
    }

    private Long resolveRequiredUserId(Authentication authentication) {
        Long userId = resolveUserId(authentication);
        if (userId == null) {
            throw new RuntimeException("Usuário não autenticado");
        }

        return userId;
    }
}
