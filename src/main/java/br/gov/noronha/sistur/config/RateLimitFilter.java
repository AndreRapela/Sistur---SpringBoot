package br.gov.noronha.sistur.config;

import br.gov.noronha.sistur.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final Map<String, WindowCounter> counters = new ConcurrentHashMap<>();
    private final AtomicLong requestCounter = new AtomicLong();

    @Value("${app.rate-limit.enabled:true}")
    private boolean enabled;

    @Value("${app.rate-limit.auth-per-minute:30}")
    private int authPerMinute;

    @Value("${app.rate-limit.registration-per-hour:20}")
    private int registrationsPerHour;

    @Value("${app.rate-limit.analytics-per-minute:600}")
    private int analyticsPerMinute;

    @Value("${app.rate-limit.compute-per-minute:120}")
    private int computePerMinute;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        RatePolicy policy = enabled ? policyFor(request) : null;
        if (policy == null) {
            filterChain.doFilter(request, response);
            return;
        }

        long now = System.currentTimeMillis();
        long windowId = now / policy.windowMillis();
        String clientId = request.getRemoteAddr() == null ? "unknown" : request.getRemoteAddr();
        String key = policy.name() + ':' + clientId;

        WindowCounter counter = counters.compute(key, (ignored, current) -> {
            if (current == null || current.windowId() != windowId) {
                return new WindowCounter(windowId, (windowId + 1) * policy.windowMillis(), new AtomicInteger(1));
            }
            current.count().incrementAndGet();
            return current;
        });

        int used = counter.count().get();
        response.setHeader("X-RateLimit-Limit", String.valueOf(policy.limit()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(0, policy.limit() - used)));

        if (used > policy.limit()) {
            long retryAfterSeconds = Math.max(1, ((windowId + 1) * policy.windowMillis() - now + 999) / 1000);
            response.setStatus(429);
            response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            Object requestIdAttribute = request.getAttribute(RequestIdFilter.ATTRIBUTE);
            String requestId = requestIdAttribute == null ? null : requestIdAttribute.toString();
            objectMapper.writeValue(
                response.getWriter(),
                ApiResponse.error("Muitas tentativas. Aguarde um momento e tente novamente.", "RATE_LIMITED", requestId)
            );
            return;
        }

        if ((requestCounter.incrementAndGet() & 1023) == 0) {
            counters.entrySet().removeIf(entry -> entry.getValue().expiresAtMillis() < now);
        }

        filterChain.doFilter(request, response);
    }

    private RatePolicy policyFor(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return null;
        }

        String path = request.getRequestURI();
        if ("/api/auth/register".equals(path)) {
            return new RatePolicy("registration", registrationsPerHour, 3_600_000L);
        }
        if ("/api/auth/login".equals(path) || "/api/auth/google".equals(path)) {
            return new RatePolicy("auth", authPerMinute, 60_000L);
        }
        if (path.startsWith("/api/analytics/")) {
            return new RatePolicy("analytics", analyticsPerMinute, 60_000L);
        }
        if (path.startsWith("/api/routes/") || path.startsWith("/api/ai/")) {
            return new RatePolicy("compute", computePerMinute, 60_000L);
        }
        return null;
    }

    private record RatePolicy(String name, int limit, long windowMillis) {
    }

    private record WindowCounter(long windowId, long expiresAtMillis, AtomicInteger count) {
    }
}
