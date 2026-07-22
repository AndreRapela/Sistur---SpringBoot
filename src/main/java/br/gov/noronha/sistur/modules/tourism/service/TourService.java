package br.gov.noronha.sistur.modules.tourism.service;

import br.gov.noronha.sistur.modules.analytics.model.AccessLog;
import br.gov.noronha.sistur.modules.analytics.repository.AccessLogRepository;
import br.gov.noronha.sistur.dto.TourDTO;
import br.gov.noronha.sistur.modules.tourism.model.Tour;
import br.gov.noronha.sistur.modules.tourism.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;
    private final AccessLogRepository accessLogRepository;

    @Cacheable(value = "tours", key = "#category + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<TourDTO> getAllTours(String category, Pageable pageable) {
        if (category != null && !category.equalsIgnoreCase("Todos")) {
            return tourRepository.findByCategory(category, pageable).map(this::toDTO);
        }
        return tourRepository.findAll(pageable).map(this::toDTO);
    }

    private TourDTO toDTO(Tour tour) {
        return new TourDTO(
            tour.getId(),
            tour.getName(),
            tour.getDescription(),
            tour.getCategory(),
            tour.getPhotoUrl(),
            tour.getPhotoCredit(),
            tour.getPrice(),
            tour.getRating(),
            tour.getReviewCount(),
            tour.getReviewSource(),
            tour.getReviewUrl(),
            tour.getPartnership(),
            tour.getContactNumber(),
            tour.getDuration(),
            tour.getSchedule(),
            tour.getMeetingPoint(),
            tour.getItinerary(),
            tour.getIncludedItems(),
            tour.getExcludedItems(),
            tour.getRequirements(),
            tour.getBookingUrl(),
            tour.getGoogleMapsUrl(),
            tour.getSourceUrl(),
            tour.getDataVerifiedAt(),
            tour.getLatitude(),
            tour.getLongitude()
        );
    }

    public TourDTO findById(Long id, Authentication authentication) {
        TourDTO tourDTO = tourRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Passeio não encontrado"));

        accessLogRepository.save(AccessLog.builder()
            .userId(resolveUserId(authentication))
            .targetType("TOUR")
            .targetId(id)
            .actionType("VIEW")
            .timestamp(LocalDateTime.now())
            .build());

        return tourDTO;
    }

    public Page<TourDTO> findByCategory(String category, Pageable pageable) {
        return tourRepository.findByCategory(category, pageable).map(this::toDTO);
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
}
