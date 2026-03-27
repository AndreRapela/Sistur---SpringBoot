package br.gov.noronha.sistur.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.EstablishmentDTO;
import br.gov.noronha.sistur.model.EstablishmentType;
import br.gov.noronha.sistur.service.EstablishmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/establishments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EstablishmentController {

    private final EstablishmentService establishmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EstablishmentDTO>>> getAllEstablishments(
            @RequestParam EstablishmentType type,
            @RequestParam(required = false) String foodType,
            Pageable pageable) {
        Page<EstablishmentDTO> ests = establishmentService.getAllEstablishments(type, foodType, pageable);
        return ResponseEntity.ok(ApiResponse.success(ests, "Estabelecimentos recuperados com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EstablishmentDTO>> getById(@PathVariable Long id) {
        EstablishmentDTO est = establishmentService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(est, "Estabelecimento recuperado com sucesso"));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<EstablishmentDTO>> getByType(@PathVariable EstablishmentType type, Pageable pageable) {
        return ResponseEntity.ok(establishmentService.findByType(type, pageable));
    }
}
