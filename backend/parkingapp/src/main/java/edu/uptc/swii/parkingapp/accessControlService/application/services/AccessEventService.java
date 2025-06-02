package edu.uptc.swii.parkingapp.accessControlService.application.services;

import edu.uptc.swii.parkingapp.accessControlService.domain.events.AccessRecordedEvent;
import edu.uptc.swii.parkingapp.accessControlService.domain.models.AccessRecord;
import edu.uptc.swii.parkingapp.accessControlService.infraestructure.messaging.producers.AccessEventProducer;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccessEventService {
    private static final Logger log = LoggerFactory.getLogger(AccessEventService.class);
    private final AccessEventProducer eventProducer;

    public AccessEventService(AccessEventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @Transactional
    public void publishAccessEvent(AccessRecord access) {
        try {
            AccessRecordedEvent event = new AccessRecordedEvent(
                access.getEmployeeId(),
                access.getAccessDateTime(),
                access.getEventType(),
                LocalDateTime.now()
            );

            String topic = "access-" + access.getEventType().toLowerCase();
            eventProducer.sendEvent(topic, event);
            
            log.info("Evento de acceso publicado: {} para empleado {}", 
                    access.getEventType(), 
                    access.getEmployeeId());
        } catch (Exception e) {
            log.error("Error al publicar evento de acceso para empleado: {}", 
                     access.getEmployeeId(), e);
            throw new RuntimeException("Error al publicar evento de acceso", e);
        }
    }
}