package edu.uptc.swii.parkingapp.accessControlService.infraestructure.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;


@Configuration
@EnableKafka
public class AccessControlKafkaConfig {
    // ... (logger and bootstrapServers)

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    private static final Logger log = LoggerFactory.getLogger(AccessControlKafkaConfig.class);


    @Bean("accessControlProducerFactory")
    public ProducerFactory<String, Object> producerFactory() {
        // ... configuration ...
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.TYPE_MAPPINGS,
            "AccessRecordedEvent:edu.uptc.swii.parkingapp.accessControlService.domain.events.AccessRecordedEvent");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean("accessControlKafkaTemplate")
    public KafkaTemplate<String, Object> kafkaTemplate(@Qualifier("accessControlProducerFactory") ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean("accessControlConsumerFactory")
    public ConsumerFactory<String, Object> consumerFactory() {
        // ... configuration ...
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "access-control-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "edu.uptc.swii.parkingapp.accessControlService.domain.events.AccessRecordedEvent");
        props.put(JsonDeserializer.TYPE_MAPPINGS,
            "AccessRecordedEvent:edu.uptc.swii.parkingapp.accessControlService.domain.events.AccessRecordedEvent");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean("accessControlKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            @Qualifier("accessControlConsumerFactory") ConsumerFactory<String, Object> consumerFactory) {
        // ... configuration ...
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
            (record, exception) -> {
                log.error("Error processing access event: {} for record key {} in topic {}",
                         exception.getMessage(), record.key(), record.topic());
            },
            new FixedBackOff(1000L, 3L)
        );
        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}