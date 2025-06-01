package edu.uptc.swii.parkingapp.employeeService.application.services;

import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeCreatedEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeDisabledEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeUpdatedEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;
import edu.uptc.swii.parkingapp.employeeService.infraestructure.messaging.producers.EmployeeEventProducer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class EmployeeEventService {
    private final EmployeeEventProducer eventProducer;

    public EmployeeEventService(EmployeeEventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

public void publishEmployeeCreated(Employee employee) {
    EmployeeCreatedEvent event = new EmployeeCreatedEvent(
        employee.getDocument(),
        employee.getFirstname(),
        employee.getLastname(),
        employee.getEmail(),
        employee.getPhone(),
        employee.isStatus(),
        LocalDateTime.now()
    );
    eventProducer.sendEvent("employee-created", event);
}


    public void publishEmployeeUpdated(Employee employee) {
        EmployeeUpdatedEvent event = new EmployeeUpdatedEvent(
            employee.getDocument(),
            employee.getFirstname(),
            employee.getLastname(),
            employee.getEmail(),
            employee.getPhone()
        );
        eventProducer.sendEvent("employee-updated", event);
    }

    public void publishEmployeeDisabled(Employee employee) {
        EmployeeDisabledEvent event = new EmployeeDisabledEvent(
            employee.getDocument()
        );
        eventProducer.sendEvent("employee-disabled", event);
    }
}