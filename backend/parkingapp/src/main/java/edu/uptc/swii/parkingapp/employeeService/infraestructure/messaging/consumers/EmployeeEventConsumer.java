package edu.uptc.swii.parkingapp.employeeService.infraestructure.messaging.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeCreatedEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeDisabledEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeUpdatedEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;
import edu.uptc.swii.parkingapp.employeeService.domain.ports.EmployeeCommandPort;
import edu.uptc.swii.parkingapp.employeeService.domain.ports.EmployeeQueryPort;

@Component
public class EmployeeEventConsumer {
    
    private final EmployeeQueryPort queryPort;
    private final EmployeeCommandPort commandPort;
    private final ObjectMapper objectMapper;

    public EmployeeEventConsumer(EmployeeQueryPort queryPort, 
                              EmployeeCommandPort commandPort,
                              ObjectMapper objectMapper) {
        this.queryPort = queryPort;
        this.commandPort = commandPort;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "employee-events", groupId = "employee-group")
    public void consumeEmployeeEvent(@Payload Object event) {
        try {
            // Convertir el evento según su tipo
            if (event instanceof EmployeeCreatedEvent) {
                handleEmployeeCreated((EmployeeCreatedEvent) event);
            } else if (event instanceof EmployeeUpdatedEvent) {
                handleEmployeeUpdated((EmployeeUpdatedEvent) event);
            } else if (event instanceof EmployeeDisabledEvent) {
                handleEmployeeDisabled((EmployeeDisabledEvent) event);
            } else {
                throw new IllegalArgumentException("Unknown event type: " + event.getClass().getName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing event", e);
        }
    }

    private void handleEmployeeCreated(EmployeeCreatedEvent event) {
        if (!queryPort.findByDocument(event.getDocument()).isPresent()) {
            Employee employee = new Employee(
                event.getDocument(),
                event.getFirstname(),
                event.getLastname(),
                event.getEmail(),
                event.getPhone(),
                event.isStatus()
            );
            commandPort.saveEmployee(employee);
        }
    }

    private void handleEmployeeUpdated(EmployeeUpdatedEvent event) {
        queryPort.findByDocument(event.getDocument())
            .ifPresent(employee -> {
                employee.update(
                    event.getFirstname(),
                    event.getLastname(),
                    event.getEmail(),
                    event.getPhone()
                );
                commandPort.updateEmployee(employee);
            });
    }

    private void handleEmployeeDisabled(EmployeeDisabledEvent event) {
        queryPort.findByDocument(event.getDocument())
            .ifPresent(employee -> {
                employee.disable();
                commandPort.disableEmployee(employee.getDocument());
            });
    }
}