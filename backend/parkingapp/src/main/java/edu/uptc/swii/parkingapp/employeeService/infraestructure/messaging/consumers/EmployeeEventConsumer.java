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
            log.debug("Evento recibido - Key: {}, Topic: {}, Partition: {}", 
                     record.key(), record.topic(), record.partition());
            
            Object event = record.value();
            
            if (event instanceof EmployeeCreatedEvent) {
                handleEmployeeCreated((EmployeeCreatedEvent) event);
            } else if (event instanceof EmployeeUpdatedEvent) {
                handleEmployeeUpdated((EmployeeUpdatedEvent) event);
            } else if (event instanceof EmployeeDisabledEvent) {
                handleEmployeeDisabled((EmployeeDisabledEvent) event);
            } else {
                log.warn("Evento desconocido recibido. Tipo: {}", event.getClass().getName());
            }
        } catch (Exception e) {
            log.error("Error crítico procesando evento. Offset: {}, Mensaje: {}", 
                     record.offset(), e.getMessage(), e);
            // No relanzamos la excepción para evitar el reintento infinito
            // Se puede implementar DLQ aquí si es necesario
        }
    }

    private void handleEmployeeCreated(EmployeeCreatedEvent event) {
        try {
            log.info("Procesando creación de empleado en MongoDB: {}", event.getDocument());
            
            if (mongoRepository.findByDocument(event.getDocument()).isPresent()) {
                log.warn("Empleado ya existe en MongoDB: {}", event.getDocument());
                return;
            }

            Employee employee = new Employee(
                event.getDocument(),
                event.getFirstname(),
                event.getLastname(),
                event.getEmail(),
                event.getPhone(),
                event.isStatus()
            );
            
            mongoRepository.save(employee);
            log.info("Empleado creado exitosamente en MongoDB: {}", event.getDocument());
        } catch (Exception e) {
            log.error("Error al crear empleado en MongoDB: {}", event.getDocument(), e);
            throw new RuntimeException("Error al crear empleado en MongoDB", e);
        }
    }

    private void handleEmployeeUpdated(EmployeeUpdatedEvent event) {
        try {
            log.info("Procesando actualización de empleado en MongoDB: {}", event.getDocument());
            
            mongoRepository.findByDocument(event.getDocument())
                .ifPresentOrElse(
                    employee -> {
                        employee.update(
                            event.getFirstname(),
                            event.getLastname(),
                            event.getEmail(),
                            event.getPhone()
                        );
                        mongoRepository.save(employee);
                        log.info("Empleado actualizado en MongoDB: {}", event.getDocument());
                    },
                    () -> log.warn("Empleado no encontrado en MongoDB para actualización: {}", event.getDocument())
                );
        } catch (Exception e) {
            log.error("Error al actualizar empleado en MongoDB: {}", event.getDocument(), e);
            throw new RuntimeException("Error al actualizar empleado en MongoDB", e);
        }
    }

    private void handleEmployeeDisabled(EmployeeDisabledEvent event) {
        try {
            log.info("Procesando deshabilitación de empleado en MongoDB: {}", event.getDocument());
            
            mongoRepository.findByDocument(event.getDocument())
                .ifPresentOrElse(
                    employee -> {
                        employee.disable();
                        mongoRepository.save(employee);
                        log.info("Empleado deshabilitado en MongoDB: {}", event.getDocument());
                    },
                    () -> log.warn("Empleado no encontrado en MongoDB para deshabilitación: {}", event.getDocument())
                );
        } catch (Exception e) {
            log.error("Error al deshabilitar empleado en MongoDB: {}", event.getDocument(), e);
            throw new RuntimeException("Error al deshabilitar empleado en MongoDB", e);
        }
    }
}