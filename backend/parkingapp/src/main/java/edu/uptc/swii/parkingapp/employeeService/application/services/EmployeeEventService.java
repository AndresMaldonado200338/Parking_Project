package edu.uptc.swii.parkingapp.employeeService.application.services;

import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeCreatedEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeDisabledEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeUpdatedEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;
import edu.uptc.swii.parkingapp.employeeService.infraestructure.messaging.producers.EmployeeEventProducer;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeEventService {
    private static final Logger log = LoggerFactory.getLogger(EmployeeEventService.class);
    private final EmployeeEventProducer eventProducer;

    public EmployeeEventService(EmployeeEventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @Transactional
    public void publishEmployeeCreated(Employee employee) {
        try {
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
            log.info("Evento de creación publicado para empleado: {}", employee.getDocument());
        } catch (Exception e) {
            log.error("Error al publicar evento de creación para empleado: {}", employee.getDocument(), e);
            throw new RuntimeException("Error al publicar evento de creación", e);
        }
    }

    @Transactional
    public void publishEmployeeUpdated(Employee employee) {
        try {
            EmployeeUpdatedEvent event = new EmployeeUpdatedEvent(
                employee.getDocument(),
                employee.getFirstname(),
                employee.getLastname(),
                employee.getEmail(),
                employee.getPhone(),
                LocalDateTime.now()
            );
            eventProducer.sendEvent("employee-updated", event);
            log.info("Evento de actualización publicado para empleado: {}", employee.getDocument());
        } catch (Exception e) {
            log.error("Error al publicar evento de actualización para empleado: {}", employee.getDocument(), e);
            throw new RuntimeException("Error al publicar evento de actualización", e);
        }
    }

    @Transactional
    public void publishEmployeeDisabled(Employee employee) {
        try {
            EmployeeDisabledEvent event = new EmployeeDisabledEvent(
                employee.getDocument(),
                LocalDateTime.now()
            );
            eventProducer.sendEvent("employee-disabled", event);
            log.info("Evento de deshabilitación publicado para empleado: {}", employee.getDocument());
        } catch (Exception e) {
            log.error("Error al publicar evento de deshabilitación para empleado: {}", employee.getDocument(), e);
            throw new RuntimeException("Error al publicar evento de deshabilitación", e);
        }
    }
}