package br.gov.noronha.sistur.service;

import br.gov.noronha.sistur.dto.EstablishmentDTO;
import br.gov.noronha.sistur.model.Establishment;
import br.gov.noronha.sistur.model.EstablishmentType;
import br.gov.noronha.sistur.repository.EstablishmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;

    public Page<EstablishmentDTO> getAllEstablishments(EstablishmentType type, String foodType, Pageable pageable) {
        if (foodType != null && !foodType.equalsIgnoreCase("Todos")) {
            // Need repository support for foodType filtering, but for now filtering by type only if default
            return establishmentRepository.findByType(type, pageable).map(this::toDTO);
        }
        return establishmentRepository.findByType(type, pageable).map(this::toDTO);
    }

    private EstablishmentDTO toDTO(Establishment e) {
        return new EstablishmentDTO(
            e.getId(), e.getName(), e.getDescription(), e.getType(),
            e.getFoodType(), e.getAveragePrice(), e.getRating(),
            e.getLocation(), e.getPhotoUrl(), e.getInstagramUrl(),
            e.getWebsiteUrl(), e.getOpeningHours(), e.getContactNumber(),
            e.getAmenities(), e.getLatitude(), e.getLongitude()
        );
    }

    @Cacheable(value = "establishments", key = "#id")
    public EstablishmentDTO findById(Long id) {
        return establishmentRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Estabelecimento não encontrado"));
    }

    public Page<EstablishmentDTO> findByType(EstablishmentType type, Pageable pageable) {
        return establishmentRepository.findByType(type, pageable).map(this::toDTO);
    }
}
