package edu.uptc.swii.parkingapp.employeeService.infraestructure.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMetrics {
    
    private final MeterRegistry registry;
    private static final String EMPLOYEE_CREATED = "employee.created";
    private static final String EMPLOYEE_UPDATED = "employee.updated";
    private static final String EMPLOYEE_DISABLED = "employee.disabled";

    public EmployeeMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void incrementEmployeeCreated() {
        registry.counter(EMPLOYEE_CREATED).increment();
    }

    public void incrementEmployeeUpdated() {
        registry.counter(EMPLOYEE_UPDATED).increment();
    }

    public void incrementEmployeeDisabled() {
        registry.counter(EMPLOYEE_DISABLED).increment();
    }
}