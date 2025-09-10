package ru.practicum.profile.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practicum.profile.event.UserRegisteredEvent;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.service.ProfileService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserRegisteredListener {
    private final ProfileService profileService;

    @KafkaListener(topics = "${app.kafka.topics.user.registration}",
            groupId = "user-service-group",
            containerFactory = "userRegisteredEventContainerFactory")
    public void onUserRegisteredEvent(UserRegisteredEvent event) {
        Profile profile = Profile.builder()
                .authProviderId(event.getUserId())
                .login(event.getUsername())
                .email(event.getEmail())
                .createdAt(LocalDateTime.now())
                .build();

        profileService.createProfile(profile);
        log.info("User registered - {}", event.getUserId());
    }
}
