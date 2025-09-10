package ru.practicum.profile.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practicum.profile.event.UserDeletedEvent;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.service.ProfileService;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserDeletedListener {
    private final ProfileService profileService;

    @KafkaListener(topics = "${app.kafka.topics.user.deleting}",
            groupId = "user-service-group",
            containerFactory = "userDeletedEventContainerFactory")
    public void userDeleted(UserDeletedEvent event) {
        Profile profile = Profile.builder()
                .authProviderId(event.getUserId())
                .deactivatedAt(event.getDeletedAt())
                .build();

        profileService.deactivateProfile(profile);
        log.info("User deleted: {}", event.getUserId());
    }
}
