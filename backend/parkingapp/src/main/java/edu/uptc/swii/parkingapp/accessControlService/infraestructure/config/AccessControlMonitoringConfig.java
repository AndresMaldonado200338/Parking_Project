package edu.uptc.swii.parkingapp.accessControlService.infraestructure.config;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class AccessControlMonitoringConfig  {
    
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> accessControlMetricsCommonTags() {
        return registry -> registry.config()
            .commonTags("application", "employee-service")
            .commonTags("module", "access-management");
    }
}