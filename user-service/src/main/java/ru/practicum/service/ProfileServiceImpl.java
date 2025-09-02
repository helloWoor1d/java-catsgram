package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.UserDeletedEvent;
import ru.practicum.event.UserRegisteredEvent;
import ru.practicum.model.Profile;
import ru.practicum.repository.ProfileRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final ProfileRepository profileRepository;

    public void create(UserRegisteredEvent event) {
        Profile profile = Profile.builder()
                .keycloakId(event.getUserId())
                .login(event.getUsername())
                .email(event.getEmail())
                .createdAt(LocalDateTime.now())
                .build();

        profileRepository.save(profile);
        log.debug("Profile service: create profile for user with email {}", event.getEmail());
    }

    public void deactivate(UserDeletedEvent event) {
        Optional<Profile> opt = profileRepository.findProfileByKeycloakId(event.getUserId());
        Profile profile = opt.orElseThrow(() ->
                new RuntimeException("Profile not found"));

        profile.setIsDeactivated(true);
        profile.setDeactivatedAt(LocalDateTime.now());
        profileRepository.save(profile);
        log.debug("Profile service: profile deactivated {}", event.getUserId());
    }
}
