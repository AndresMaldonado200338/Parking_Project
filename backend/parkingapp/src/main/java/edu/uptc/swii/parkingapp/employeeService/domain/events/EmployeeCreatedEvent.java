package edu.uptc.swii.parkingapp.employeeService.domain.events;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("EmployeeCreatedEvent")
public class EmployeeCreatedEvent implements DomainEvent {
    private String document;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private boolean status;
    private LocalDateTime occurredOn;

    @Override
    public String getType() {
        return "EmployeeCreatedEvent";
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}