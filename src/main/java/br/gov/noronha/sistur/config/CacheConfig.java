package br.gov.noronha.sistur.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "establishments",
                "events",
                "tours",
                "touristPoints",
                "items",
                "ai_recommendations",
                "public_itineraries"
        );
    }
}
