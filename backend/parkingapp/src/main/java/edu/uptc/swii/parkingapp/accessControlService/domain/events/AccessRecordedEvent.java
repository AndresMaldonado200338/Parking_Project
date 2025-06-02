package edu.uptc.swii.parkingapp.accessControlService.domain.events;

import java.time.LocalDateTime;

public record AccessRecordedEvent(
    String employeeId,
    LocalDateTime accessDateTime,
    String eventType, // "ENTRADA" o "SALIDA"
    LocalDateTime occurredOn
) implements DomainEvent {
    @Override
    public String getType() {
        return "AccessRecordedEvent";
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}