package edu.uptc.swii.parkingapp.accessControlService.domain.models;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Getter;

@Getter
public class AccessRecord {
    private final String employeeId;
    private final LocalDateTime accessDateTime;
    private final String eventType; // "ENTRADA" o "SALIDA"

    public AccessRecord(String employeeId, LocalDateTime accessDateTime, String eventType) {
        this.employeeId = Objects.requireNonNull(employeeId);
        this.accessDateTime = Objects.requireNonNull(accessDateTime);
        this.eventType = validateEventType(eventType);
    }

    private String validateEventType(String type) {
        if (!"ENTRADA".equals(type) && !"SALIDA".equals(type)) {
            throw new IllegalArgumentException("Invalid event type");
        }
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessRecord that = (AccessRecord) o;
        return employeeId.equals(that.employeeId) && 
               accessDateTime.equals(that.accessDateTime) &&
               eventType.equals(that.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, accessDateTime, eventType);
    }
}