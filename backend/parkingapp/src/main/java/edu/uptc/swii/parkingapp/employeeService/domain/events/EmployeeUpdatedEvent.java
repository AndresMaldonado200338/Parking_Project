package edu.uptc.swii.parkingapp.employeeService.domain.events;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmployeeUpdatedEvent implements DomainEvent {
    private final String document;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String phone;
    private final LocalDateTime occurredOn = LocalDateTime.now();

    @Override
    public String getType() {
        return "EmployeeUpdatedEvent";
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}