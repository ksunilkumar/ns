package com.nsind.common.config;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Observability Configuration
 * Provides logging, metrics, and tracing setup
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ObservabilityConfig {

    private final MeterRegistry meterRegistry;

    /**
     * Configure application metrics
     */
    @Bean
    public void configureMetrics() {
        // This bean initializes metrics collection
        // Metrics are collected via Micrometer and exported to Prometheus
    }

    /**
     * Health check indicator
     */
    @Bean
    public ApplicationHealthIndicator applicationHealthIndicator() {
        return new ApplicationHealthIndicator();
    }
}

