package edu.uptc.swii.parkingapp.employeeService.domain.events;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("EmployeeDisabledEvent")
public class EmployeeDisabledEvent implements DomainEvent {
    private String document;
    private LocalDateTime occurredOn;

    @Override
    public String getType() {
        return "EmployeeDisabledEvent";
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}