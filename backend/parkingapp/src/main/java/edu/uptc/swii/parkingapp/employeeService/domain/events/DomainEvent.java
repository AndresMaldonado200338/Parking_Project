package edu.uptc.swii.parkingapp.employeeService.domain.events;

import java.time.LocalDateTime;

public interface DomainEvent {
    String getType();
    LocalDateTime getOccurredOn();
}