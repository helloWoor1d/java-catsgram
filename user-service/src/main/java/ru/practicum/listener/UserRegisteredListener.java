package ru.practicum.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practicum.event.UserRegisteredEvent;

@Slf4j
@Service
public class UserRegisteredListener {
    @KafkaListener(topics = "${app.kafka.topics.user.registration}",
            groupId = "user-service-group",
            containerFactory = "userRegisteredEventContainerFactory")
    public void onUserRegisteredEvent(UserRegisteredEvent event) {
        log.info("Listener: userRegisteredEvent - {}", event.getUsername());
    }
}
