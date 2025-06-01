package edu.uptc.swii.parkingapp.employeeService.infraestructure.messaging.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeCreatedEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeDisabledEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeUpdatedEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;
import edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.mongodb.MongoEmployeeRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmployeeEventConsumer {

    private final MongoEmployeeRepository mongoRepository;

    public EmployeeEventConsumer(MongoEmployeeRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @KafkaListener(topics = "employee-events", groupId = "employee-group",
                  containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeEmployeeEvent(ConsumerRecord<String, Object> record) {
        try {
            Object event = record.value();
            
            if (event instanceof EmployeeCreatedEvent) {
                handleEmployeeCreated((EmployeeCreatedEvent) event);
            } else if (event instanceof EmployeeUpdatedEvent) {
                handleEmployeeUpdated((EmployeeUpdatedEvent) event);
            } else if (event instanceof EmployeeDisabledEvent) {
                handleEmployeeDisabled((EmployeeDisabledEvent) event);
            } else {
                log.warn("Evento desconocido recibido: {}", event.getClass().getName());
            }
        } catch (Exception e) {
            log.error("Error procesando evento: {}", e.getMessage());
            throw new RuntimeException("Error procesando evento", e);
        }
    }

    private void handleEmployeeCreated(EmployeeCreatedEvent event) {
        log.info("Procesando EmployeeCreatedEvent: {}", event.getDocument());
        Employee employee = new Employee(
            event.getDocument(),
            event.getFirstname(),
            event.getLastname(),
            event.getEmail(),
            event.getPhone(),
            event.isStatus()
        );
        mongoRepository.save(employee);
    }

    private void handleEmployeeUpdated(EmployeeUpdatedEvent event) {
        log.info("Procesando EmployeeUpdatedEvent: {}", event.getDocument());
        mongoRepository.findByDocument(event.getDocument())
            .ifPresent(employee -> {
                employee.update(
                    event.getFirstname(),
                    event.getLastname(),
                    event.getEmail(),
                    event.getPhone()
                );
                mongoRepository.save(employee);
            });
    }

    private void handleEmployeeDisabled(EmployeeDisabledEvent event) {
        log.info("Procesando EmployeeDisabledEvent: {}", event.getDocument());
        mongoRepository.findByDocument(event.getDocument())
            .ifPresent(employee -> {
                employee.disable();
                mongoRepository.save(employee);
            });
    }
}