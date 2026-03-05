package com.nsind.common.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Resilience4j Configuration
 * Provides circuit breakers, retries, and time limiters for external API calls
 */
@Configuration
public class ResilienceConfig {

    /**
     * Circuit Breaker Configuration for External APIs
     */
    @Bean
    public CircuitBreaker externalApiCircuitBreaker(CircuitBreakerRegistry registry) {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50.0f) // 50% failure rate triggers circuit break
                .slowCallRateThreshold(50.0f) // 50% slow calls trigger circuit break
                .slowCallDurationThreshold(Duration.ofSeconds(2))
                .waitDurationInOpenState(Duration.ofSeconds(30)) // Wait 30s before trying again
                .permittedNumberOfCallsInHalfOpenState(3) // Try 3 times in half-open state
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .recordExceptions(Exception.class)
                .ignoreExceptions(IllegalArgumentException.class)
                .build();

        return registry.circuitBreaker("externalApiCircuitBreaker", config);
    }

    /**
     * Retry Configuration for Transient Failures
     */
    @Bean
    public Retry standardRetry(RetryRegistry registry) {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3) // Retry up to 3 times
                .waitDuration(Duration.ofMillis(500)) // Wait 500ms between retries
                .intervalFunction(io.github.resilience4j.core.IntervalFunction
                        .ofExponentialBackoff(500, 2)) // Exponential backoff: 500ms, 1s, 2s
                .retryOnException(e -> e instanceof Exception)
                .build();

        return registry.retry("standardRetry", config);
    }

    /**
     * Retry Configuration for Fast APIs (Quick retries)
     */
    @Bean
    public Retry fastApiRetry(RetryRegistry registry) {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(2) // Retry up to 2 times
                .waitDuration(Duration.ofMillis(100))
                .retryOnException(e -> e instanceof Exception)
                .build();

        return registry.retry("fastApiRetry", config);
    }

    /**
     * Time Limiter Configuration
     */
    @Bean
    public TimeLimiter externalApiTimeLimiter(TimeLimiterRegistry registry) {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(5)) // 5 second timeout
                .cancelRunningFuture(true)
                .build();

        return registry.timeLimiter("externalApiTimeLimiter", config);
    }

    /**
     * Time Limiter for slow operations
     */
    @Bean
    public TimeLimiter slowOperationTimeLimiter(TimeLimiterRegistry registry) {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(30)) // 30 second timeout
                .cancelRunningFuture(true)
                .build();

        return registry.timeLimiter("slowOperationTimeLimiter", config);
    }
}

