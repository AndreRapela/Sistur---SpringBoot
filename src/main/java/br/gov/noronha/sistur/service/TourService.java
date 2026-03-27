package br.gov.noronha.sistur.service;

import br.gov.noronha.sistur.dto.TourDTO;
import br.gov.noronha.sistur.model.Tour;
import br.gov.noronha.sistur.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;

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
            tour.getPrice(),
            tour.getPartnership(),
            tour.getContactNumber(),
            tour.getLatitude(),
            tour.getLongitude()
        );
    }

    @Cacheable(value = "tours", key = "#id")
    public TourDTO findById(Long id) {
        return tourRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Passeio não encontrado"));
    }

    public Page<TourDTO> findByCategory(String category, Pageable pageable) {
        return tourRepository.findByCategory(category, pageable).map(this::toDTO);
    }
}
