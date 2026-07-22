package br.gov.noronha.sistur.modules.weather.controller;

import br.gov.noronha.sistur.dto.ApiResponse;
import br.gov.noronha.sistur.modules.weather.dto.WeatherGatewayResponse;
import br.gov.noronha.sistur.modules.weather.service.WeatherGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherGatewayService weatherGatewayService;

    @GetMapping("/noronha")
    public ResponseEntity<ApiResponse<WeatherGatewayResponse>> noronha() {
        return ResponseEntity.ok(ApiResponse.success(
            weatherGatewayService.getNoronhaWeather(),
            "Previsão climática de Noronha atualizada"
        ));
    }
}
