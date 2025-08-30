package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.event.UserDeletedEvent;
import ru.practicum.event.UserRegisteredEvent;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, UserRegisteredEvent> registerTemplate;
    private final KafkaTemplate<String, UserDeletedEvent> deleteTemplate;

    public void userRegister(String id, String email, String username) {
        UserRegisteredEvent userRegistered = UserRegisteredEvent.builder()
                .userId(id)
                .email(email)
                .username(username)
                .createdAt(LocalDateTime.now())
                .build();

        registerTemplate.send("user-registration", userRegistered);
        log.debug("Kafka: event UserRegistered success published: {}", userRegistered);
    }

    public void userDelete(String id) {
        UserDeletedEvent userDeleteEvent = UserDeletedEvent.builder()
                .userId(id)
                .deletedAt(LocalDateTime.now())
                .build();

        deleteTemplate.send("user-deleting", userDeleteEvent);
        log.debug("Kafka: event UserDeleted success published: {}", userDeleteEvent.getUserId());
    }
}
