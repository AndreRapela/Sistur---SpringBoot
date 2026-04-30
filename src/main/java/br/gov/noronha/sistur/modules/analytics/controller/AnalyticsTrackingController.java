package br.gov.noronha.sistur.modules.analytics.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.dto.TrackEventRequest;
import br.gov.noronha.sistur.modules.analytics.service.AnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnalyticsTrackingController {

    private final AnalyticsService analyticsService;

    @PostMapping("/track")
    public ResponseEntity<ApiResponse<Void>> track(@RequestBody TrackEventRequest request,
                                                   Authentication authentication,
                                                   HttpServletRequest servletRequest) {
        analyticsService.recordEvent(request, authentication, servletRequest.getRemoteAddr());
        return ResponseEntity.ok(ApiResponse.success(null, "Evento registrado"));
    }
}