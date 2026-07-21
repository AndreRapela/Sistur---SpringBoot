package br.gov.noronha.sistur.dto;

import java.util.List;
import java.util.Map;

public record AdminAnalyticsDTO(
    long totalUsers,
    long activeUsersLast30Days,
    long registrationsLast30Days,
    long totalRequests,
    long totalConversions,
    long requestsLast30Days,
    long conversionsLast30Days,
    long googleServiceClicks,
    long googleCategoryClicks,
    long itineraryAdds,
    long detailOpens,
    long pageViews,
    long searchEvents,
    double conversionRate,
    double googleConversionRate,
    List<MetricEntryDTO> categoryDemand,
    List<MetricEntryDTO> conversionByCategory,
    List<MetricEntryDTO> topGoogleServiceClicks,
    List<MetricEntryDTO> topViewedItems,
    List<DailyMetricDTO> dailyRegistrations,
    List<DailyMetricDTO> dailyGoogleClicks,
    List<DailyMetricDTO> dailyRequests,
    Map<String, Long> funnel
) {
    public record MetricEntryDTO(
        String key,
        String label,
        long value,
        double rate
    ) {}

    public record DailyMetricDTO(
        String date,
        long value
    ) {}
}
