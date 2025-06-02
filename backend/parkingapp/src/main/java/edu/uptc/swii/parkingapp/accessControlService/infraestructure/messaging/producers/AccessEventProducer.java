package edu.uptc.swii.parkingapp.accessControlService.infraestructure.messaging.producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.uptc.swii.parkingapp.accessControlService.domain.events.AccessRecordedEvent;
import lombok.extern.slf4j.Slf4j; 

@Slf4j
@Component
@Transactional
public class AccessEventProducer {
    
    private static final String DLQ_TOPIC = "access-events-dlq";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AccessEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String topic, Object event) {
        try {
            // Ensure event is an instance of AccessRecordedEvent before casting
            if (event instanceof AccessRecordedEvent) {
                AccessRecordedEvent accessEvent = (AccessRecordedEvent) event;
                String eventType = accessEvent.eventType(); // Use the getter from the record
                
                Message<Object> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .setHeader(KafkaHeaders.KEY, eventType)
                    .setHeader("event-type", "AccessRecordedEvent") // Consider using accessEvent.getClass().getSimpleName() for robustness
                    .build();
                
                kafkaTemplate.send(message)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Error enviando evento de acceso a Kafka: {}", eventType, ex);
                            sendToDlq(topic, event); // Pass original topic
                        }
                    });
            } else {
                log.error("Attempted to send an event that is not an AccessRecordedEvent: {}", event.getClass().getName());
                // Optionally, send to a generic DLQ or handle differently
            }
        } catch (Exception e) {
            log.error("Error crítico al enviar evento de acceso", e);
            // Ensure event is an instance of AccessRecordedEvent before casting in sendToDlq
            if (event instanceof AccessRecordedEvent) {
                 sendToDlq(topic, event); // Pass original topic
            } else {
                log.error("Cannot send non-AccessRecordedEvent to DLQ from sendEvent general catch block.");
            }
        }
    }

    private void sendToDlq(String originalTopic, Object event) {
        try {
            // Ensure event is an instance of AccessRecordedEvent before casting
             if (event instanceof AccessRecordedEvent) {
                AccessRecordedEvent accessEvent = (AccessRecordedEvent) event;
                String eventType = accessEvent.eventType(); // Use the getter from the record
                
                Message<Object> dlqMessage = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, DLQ_TOPIC)
                    .setHeader(KafkaHeaders.KEY, eventType)
                    .setHeader("original-topic", originalTopic)
                    .build();
                
                kafkaTemplate.send(dlqMessage);
            } else {
                 log.error("Attempted to send an event to DLQ that is not an AccessRecordedEvent: {}", event.getClass().getName());
            }
        } catch (Exception dlqEx) {
            log.error("Error CRÍTICO al enviar a DLQ de accesos", dlqEx);
        }
    }
}