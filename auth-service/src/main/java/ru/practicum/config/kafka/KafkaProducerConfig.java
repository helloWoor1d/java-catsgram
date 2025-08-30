package ru.practicum.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.practicum.event.UserDeletedEvent;
import ru.practicum.event.UserRegisteredEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, UserRegisteredEvent> userRegisterProducerFactory(ObjectMapper objectMapper) {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        JsonSerializer<UserRegisteredEvent> eventSerializer = new JsonSerializer<>(objectMapper);
        eventSerializer.setAddTypeInfo(false);

        return new DefaultKafkaProducerFactory<>(props,
                new StringSerializer(),
                eventSerializer);
    }

    @Bean
    public ProducerFactory<String, UserDeletedEvent> userDeleteEventProducerFactory(ObjectMapper objectMapper) {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        JsonSerializer<UserDeletedEvent> eventSerializer = new JsonSerializer<>(objectMapper);
        eventSerializer.setAddTypeInfo(false);

        return new DefaultKafkaProducerFactory<>(props,
                new StringSerializer(),
                eventSerializer);
    }

    @Bean
    public KafkaTemplate<String, UserRegisteredEvent> registerTemplate(
            ProducerFactory<String, UserRegisteredEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTemplate<String, UserDeletedEvent> deleteTemplate(
            ProducerFactory<String, UserDeletedEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
