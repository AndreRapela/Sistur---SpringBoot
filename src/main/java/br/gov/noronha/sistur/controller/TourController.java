package br.gov.noronha.sistur.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.TourDTO;
import br.gov.noronha.sistur.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tours")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TourController {

    private final TourService tourService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TourDTO>>> getAllTours(
            @RequestParam(required = false) String category,
            Pageable pageable) {
        Page<TourDTO> tours = tourService.getAllTours(category, pageable);
        return ResponseEntity.ok(ApiResponse.success(tours, "Passeios recuperados com sucesso"));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<TourDTO>> getByCategory(@PathVariable String category, Pageable pageable) {
        return ResponseEntity.ok(tourService.findByCategory(category, pageable));
    }
}
