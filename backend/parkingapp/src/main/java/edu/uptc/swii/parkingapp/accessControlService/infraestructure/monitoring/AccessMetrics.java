package edu.uptc.swii.parkingapp.accessControlService.infraestructure.monitoring;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;

@Component
public class AccessMetrics {
    
    private final MeterRegistry registry;
    private static final String ACCESS_CHECK_IN = "access.checkin";
    private static final String ACCESS_CHECK_OUT = "access.checkout";
    private static final String ACCESS_DURATION = "access.duration";
    private static final String ACCESS_VALIDATION_ERROR = "access.validation.error";

    public AccessMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void incrementCheckIn() {
        registry.counter(ACCESS_CHECK_IN).increment();
    }

    public void incrementCheckOut() {
        registry.counter(ACCESS_CHECK_OUT).increment();
    }

    public void recordAccessDuration(long durationInSeconds) {
        registry.summary(ACCESS_DURATION)
               .record(durationInSeconds);
    }

    public void incrementValidationError(String errorType) {
        registry.counter(ACCESS_VALIDATION_ERROR, 
                        "type", errorType)
               .increment();
    }

    public void trackEmployeeAccess(String employeeId) {
        registry.counter("access.by.employee", 
                        "employeeId", employeeId)
               .increment();
    }
}