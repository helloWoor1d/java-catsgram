package ru.practicum.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practicum.event.UserDeletedEvent;
import ru.practicum.service.UserService;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserDeletedListener {
    private final UserService userService;

    @KafkaListener(topics = "${app.kafka.topics.user.deleting}",
            groupId = "user-service-group",
            containerFactory = "userDeletedEventContainerFactory")
    public void userDeleted(UserDeletedEvent event) {
        userService.deactivate(event);
        log.info("Listener: user deleted: {}", event.getUserId());
    }
}
