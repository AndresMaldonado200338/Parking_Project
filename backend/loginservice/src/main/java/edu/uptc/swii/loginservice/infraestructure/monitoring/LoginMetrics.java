package edu.uptc.swii.loginservice.infraestructure.monitoring;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;

@Component
public class LoginMetrics {

    private final MeterRegistry registry;
    private static final String LOGIN_SUCCESS = "login.success";
    private static final String LOGIN_FAILURE = "login.failure";
    private static final String LOGIN_REGISTRATION_SUCCESS = "login.registration.success";
    private static final String LOGIN_REGISTRATION_ERROR = "login.registration.error";
    private static final String LOGIN_DURATION = "login.duration";

    public LoginMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void incrementLoginSuccess() {
        registry.counter(LOGIN_SUCCESS).increment();
    }

    public void incrementLoginFailure(String reason) {
        registry.counter(LOGIN_FAILURE, "reason", reason).increment();
    }

    public void incrementRegistrationSuccess() {
        registry.counter(LOGIN_REGISTRATION_SUCCESS).increment();
    }

    public void incrementRegistrationError(String reason) {
        registry.counter(LOGIN_REGISTRATION_ERROR, "reason", reason).increment();
    }

    public void recordLoginDuration(long durationMillis) {
        registry.summary(LOGIN_DURATION).record(durationMillis);
    }
}