package ru.practicum.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practicum.event.UserRegisteredEvent;
import ru.practicum.service.ProfileService;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserRegisteredListener {
    private final ProfileService profileService;

    @KafkaListener(topics = "${app.kafka.topics.user.registration}",
            groupId = "user-service-group",
            containerFactory = "userRegisteredEventContainerFactory")
    public void onUserRegisteredEvent(UserRegisteredEvent event) {
        profileService.createProfile(event);
        log.info("Listener: user registered - {}", event.getUserId());
    }
}
