package edu.uptc.swii.parkingapp.employeeService.domain.events;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class EmployeeCreatedEvent implements DomainEvent {
    private final String document;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String phone;
    private final boolean status;
    private final LocalDateTime occurredOn;

    @JsonCreator
    public EmployeeCreatedEvent(
            @JsonProperty("document") String document,
            @JsonProperty("firstname") String firstname,
            @JsonProperty("lastname") String lastname,
            @JsonProperty("email") String email,
            @JsonProperty("phone") String phone,
            @JsonProperty("status") boolean status,
            @JsonProperty("occurredOn") LocalDateTime occurredOn) {
        this.document = document;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.occurredOn = occurredOn != null ? occurredOn : LocalDateTime.now();
    }

    @Override
    public String getType() {
        return "EmployeeCreatedEvent";
    }

    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
}