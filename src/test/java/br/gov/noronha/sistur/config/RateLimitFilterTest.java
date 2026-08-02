package br.gov.noronha.sistur.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RateLimitFilterTest {

    private RateLimitFilter filter;

    @BeforeEach
    void setUp() {
        filter = new RateLimitFilter(new ObjectMapper().findAndRegisterModules());
        ReflectionTestUtils.setField(filter, "enabled", true);
        ReflectionTestUtils.setField(filter, "authPerMinute", 2);
        ReflectionTestUtils.setField(filter, "registrationsPerHour", 1);
        ReflectionTestUtils.setField(filter, "analyticsPerMinute", 2);
        ReflectionTestUtils.setField(filter, "computePerMinute", 2);
    }

    @Test
    void blocksRequestsAboveTheEndpointLimit() throws Exception {
        FilterChain chain = mock(FilterChain.class);

        MockHttpServletResponse firstResponse = execute("/api/auth/register", "203.0.113.8", chain);
        MockHttpServletResponse secondResponse = execute("/api/auth/register", "203.0.113.8", chain);

        assertThat(firstResponse.getStatus()).isEqualTo(200);
        assertThat(secondResponse.getStatus()).isEqualTo(429);
        assertThat(secondResponse.getHeader("Retry-After")).isNotBlank();
        assertThat(secondResponse.getContentAsString()).contains("RATE_LIMITED");
        verify(chain, times(1)).doFilter(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void keepsCountersSeparateByClient() throws Exception {
        FilterChain chain = mock(FilterChain.class);

        MockHttpServletResponse firstClient = execute("/api/auth/register", "203.0.113.8", chain);
        MockHttpServletResponse secondClient = execute("/api/auth/register", "203.0.113.9", chain);

        assertThat(firstClient.getStatus()).isEqualTo(200);
        assertThat(secondClient.getStatus()).isEqualTo(200);
        verify(chain, times(2)).doFilter(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    private MockHttpServletResponse execute(String path, String remoteAddress, FilterChain chain) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", path);
        request.setRemoteAddr(remoteAddress);
        request.setAttribute(RequestIdFilter.ATTRIBUTE, "test-request-id");
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, chain);
        return response;
    }
}
