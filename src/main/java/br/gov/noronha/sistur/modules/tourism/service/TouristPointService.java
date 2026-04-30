package br.gov.noronha.sistur.modules.tourism.service;

import br.gov.noronha.sistur.dto.TouristPointDTO;
import br.gov.noronha.sistur.modules.tourism.model.TouristPoint;
import br.gov.noronha.sistur.modules.tourism.repository.TouristPointRepository;
import br.gov.noronha.sistur.repository.specification.TouristPointSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TouristPointService {

    private final TouristPointRepository touristPointRepository;

    @Cacheable(value = "touristPoints", key = "#category + '-' + #search + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<TouristPointDTO> getAllTouristPoints(String category, String search, Pageable pageable) {
        Specification<TouristPoint> spec = Specification.where(TouristPointSpecifications.hasCategory(category))
            .and(TouristPointSpecifications.searchByNameOrDescription(search));

        return touristPointRepository.findAll(spec, pageable).map(this::toDTO);
    }

    @Cacheable(value = "touristPoints", key = "'detail-' + #id")
    public TouristPointDTO getTouristPointById(Long id) {
        TouristPoint point = touristPointRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ponto turístico não encontrado"));

        return toDTO(point);
    }

    private TouristPointDTO toDTO(TouristPoint point) {
        return new TouristPointDTO(
            point.getId(),
            point.getName(),
            point.getDescription(),
            point.getCategory(),
            point.getLocation(),
            point.getPhotoUrl(),
            point.getAccessType(),
            point.getRequiresTicket(),
            point.getRequiresGuide(),
            point.getBestTime(),
            point.getLatitude(),
            point.getLongitude()
        );
    }
}