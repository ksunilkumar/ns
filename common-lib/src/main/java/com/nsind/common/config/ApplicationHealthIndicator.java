package com.nsind.common.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom Health Indicator for application health monitoring
 */
@Component
public class ApplicationHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        try {
            // Check various system components
            Map<String, Object> components = new HashMap<>();

            // Check database connectivity (example)
            components.put("database", "UP");

            // Check external APIs (example)
            components.put("externalApis", "UP");

            // Check cache (example)
            components.put("cache", "UP");

            return Health.up()
                    .withDetails(components)
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}

