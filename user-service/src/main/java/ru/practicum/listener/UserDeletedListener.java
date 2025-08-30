package ru.practicum.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practicum.event.UserDeletedEvent;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserDeletedListener {
    @KafkaListener(topics = "${app.kafka.topics.user.deleting}",
            groupId = "user-service-group",
            containerFactory = "userDeletedEventContainerFactory")
    public void userDeleted(UserDeletedEvent deletedEvent) {
        log.info("Listener: user deleted: {}", deletedEvent);
    }
}
