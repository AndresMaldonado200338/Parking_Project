package edu.uptc.swii.parkingapp.employeeService.infraestructure.messaging.producers;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
public class EmployeeEventProducer {
    
    private static final String TOPIC = "employee-events";
    private static final String DLQ_TOPIC = "employee-events-dlq";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EmployeeEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(String eventType, Object event) {
        try {
            Message<Object> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .setHeader(KafkaHeaders.KEY, eventType)
                .setHeader("event-type", eventType)
                .build();
            
            kafkaTemplate.send(message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error enviando evento a Kafka: {}", eventType, ex);
                        sendToDlq(eventType, event);
                    }
                });
        } catch (Exception e) {
            log.error("Error crítico al enviar evento: {}", eventType, e);
            sendToDlq(eventType, event);
        }
    }

    private void sendToDlq(String eventType, Object event) {
        try {
            Message<Object> dlqMessage = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, DLQ_TOPIC)
                .setHeader(KafkaHeaders.KEY, eventType)
                .build();
            
            kafkaTemplate.send(dlqMessage);
        } catch (Exception dlqEx) {
            log.error("Error CRÍTICO al enviar a DLQ: {}", eventType, dlqEx);
        }
    }
}