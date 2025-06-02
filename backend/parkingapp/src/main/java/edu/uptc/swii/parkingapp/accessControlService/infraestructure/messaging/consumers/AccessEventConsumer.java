package edu.uptc.swii.parkingapp.accessControlService.infraestructure.messaging.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.uptc.swii.parkingapp.accessControlService.domain.events.AccessRecordedEvent;
import edu.uptc.swii.parkingapp.accessControlService.domain.models.AccessRecord;
import edu.uptc.swii.parkingapp.accessControlService.infraestructure.persistence.mongodb.MongoAccessRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AccessEventConsumer {

    private final MongoAccessRepository mongoRepository;

    public AccessEventConsumer(MongoAccessRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @KafkaListener(topics = {"access-entrada", "access-salida"}, 
                  groupId = "access-control-group",
                  containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consumeAccessEvent(ConsumerRecord<String, Object> record) {
        try {
            log.debug("Evento de acceso recibido - Key: {}, Topic: {}, Partition: {}", 
                     record.key(), record.topic(), record.partition());
            
            Object event = record.value();
            
            if (event instanceof AccessRecordedEvent) {
                handleAccessRecord((AccessRecordedEvent) event);
            } else {
                log.warn("Evento desconocido recibido en topic de acceso. Tipo: {}", 
                        event != null ? event.getClass().getName() : "null");
            }
        } catch (Exception e) {
            log.error("Error procesando evento de acceso. Offset: {}, Mensaje: {}", 
                     record.offset(), e.getMessage(), e);
            // Podría implementarse DLQ específico para accesos
        }
    }

    private void handleAccessRecord(AccessRecordedEvent event) {
        try {
            log.info("Procesando {} en MongoDB para empleado: {}", 
                    event.eventType(), event.employeeId());
            
            AccessRecord access = new AccessRecord(
                event.employeeId(),
                event.accessDateTime(),
                event.eventType()
            );
            
            mongoRepository.save(access);
            log.info("Registro de acceso guardado en MongoDB: {} - {}", 
                    event.employeeId(), event.eventType());
        } catch (Exception e) {
            log.error("Error al procesar acceso en MongoDB: {} - {}", 
                     event.employeeId(), event.eventType(), e);
            throw new RuntimeException("Error al procesar acceso en MongoDB", e);
        }
    }
}