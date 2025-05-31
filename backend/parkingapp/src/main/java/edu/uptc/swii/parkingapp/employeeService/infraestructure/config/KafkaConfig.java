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
import org.springframework.kafka.support.serializer.JsonSerializer;

import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeCreatedEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeDisabledEvent;
import edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeUpdatedEvent;

import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
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
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, 
            "edu.uptc.swii.parkingapp.employeeService.domain.events");
        props.put(JsonDeserializer.TYPE_MAPPINGS,
            "EmployeeCreatedEvent:edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeCreatedEvent," +
            "EmployeeUpdatedEvent:edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeUpdatedEvent," +
            "EmployeeDisabledEvent:edu.uptc.swii.parkingapp.employeeService.domain.events.EmployeeDisabledEvent");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), 
            new JsonDeserializer<>(Object.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setRecordFilterStrategy(record -> {
            try {
                Object value = record.value();
                return !(value instanceof EmployeeCreatedEvent || 
                       value instanceof EmployeeUpdatedEvent || 
                       value instanceof EmployeeDisabledEvent);
            } catch (Exception e) {
                return true;
            }
        });
        return factory;
    }
}