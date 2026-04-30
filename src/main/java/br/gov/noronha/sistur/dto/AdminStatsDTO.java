package br.gov.noronha.sistur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsDTO {
    private long totalUsers;
    private long activeUsersLast30Days;
    private long registrationsLast30Days;
    private long totalRequests;
    private long totalConversions;
    private Map<String, Long> accessByEstablishment;
    private Map<String, Long> conversionByEstablishment;
}
