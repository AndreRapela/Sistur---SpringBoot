package br.gov.noronha.sistur.dto;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class RouteRequestDTO {
    @NotEmpty
    @Size(min = 2, max = 25)
    private List<@Valid LocationDTO> waypoints;

    @Pattern(regexp = "WALKING|DRIVING|BICYCLING", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String travelMode; // WALKING, DRIVING, BICYCLING
}
