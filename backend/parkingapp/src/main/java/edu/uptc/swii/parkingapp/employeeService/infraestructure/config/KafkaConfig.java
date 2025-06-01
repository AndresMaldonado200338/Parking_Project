package edu.uptc.swii.parkingapp.employeeService.infraestructure.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.TYPE_MAPPINGS, 
            "EmployeeCreatedEvent:edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeCreatedEvent," +
            "EmployeeUpdatedEvent:edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeUpdatedEvent," +
            "EmployeeDisabledEvent:edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeDisabledEvent");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "employee-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeCreatedEvent");
        props.put(JsonDeserializer.TYPE_MAPPINGS,
            "EmployeeCreatedEvent:edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeCreatedEvent," +
            "EmployeeUpdatedEvent:edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeUpdatedEvent," +
            "EmployeeDisabledEvent:edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeDisabledEvent");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
            (record, exception) -> {
                log.error("Error procesando mensaje: {}", exception.getMessage());
            },
            new FixedBackOff(1000L, 3L)
        );
        errorHandler.addNotRetryableExceptions(IllegalStateException.class);
        factory.setCommonErrorHandler(errorHandler);
        
        return factory;
    }
}