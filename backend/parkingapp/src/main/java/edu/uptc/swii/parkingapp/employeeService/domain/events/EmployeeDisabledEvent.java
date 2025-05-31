package edu.uptc.swii.parkingapp.employeeService.domain.events;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmployeeDisabledEvent implements DomainEvent {
    private final String document;
    private final LocalDateTime occurredOn = LocalDateTime.now();

    @Override
    public String getType() {
        return "EmployeeDisabledEvent";
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}