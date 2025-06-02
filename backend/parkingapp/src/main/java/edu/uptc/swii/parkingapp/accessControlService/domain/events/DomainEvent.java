package edu.uptc.swii.parkingapp.accessControlService.domain.events;

import java.time.LocalDateTime;

public interface DomainEvent {
    String getType();
    LocalDateTime getOccurredOn();
}