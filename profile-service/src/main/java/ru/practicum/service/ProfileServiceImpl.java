package ru.practicum.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.UserDeletedEvent;
import ru.practicum.event.UserRegisteredEvent;
import ru.practicum.model.Profile;
import ru.practicum.repository.ProfileRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;

    public Profile getCurrentProfile(String keycloakId) {
        log.debug("Get current profile keycloakId: {}", keycloakId);
        return profileRepository.findProfileByAuthProviderId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException("No profile found for keycloakId: " + keycloakId));
    }

    public Profile getProfile(String login) {
        log.debug("Get profile by login: {}", login);
        return profileRepository.findProfileByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("No profile found for login: " + login));
    }

    public void createProfile(UserRegisteredEvent event) {
        Profile profile = Profile.builder()
                .authProviderId(event.getUserId())
                .login(event.getUsername())
                .email(event.getEmail())
                .createdAt(LocalDateTime.now())
                .build();

        profileRepository.save(profile);
        log.debug("Create profile for user with email {}", event.getEmail());
    }

    public void deactivateProfile(UserDeletedEvent event) {
        Profile profile = profileRepository.findProfileByAuthProviderId(event.getUserId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Profile not found"));

        profile.setDeactivated(true);
        profile.setDeactivatedAt(LocalDateTime.now());
        profileRepository.save(profile);
        log.debug("Profile deactivated {}", event.getUserId());
    }

    public Profile updateProfile(Profile profile) {
        log.debug("Update profile {}", profile.getAuthProviderId());
        Profile saved = profileRepository.findProfileByAuthProviderId(profile.getAuthProviderId())
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        if(profile.getLogin() != null && !profile.getLogin().isBlank()) {         // toDo: mapstruct
            saved.setLogin(profile.getLogin());
        } if(profile.getBio() != null && !profile.getBio().isBlank()) {
            saved.setBio(profile.getBio());
        } if(profile.getAvatarUrl() != null && !profile.getEmail().isBlank()) {
            saved.setAvatarUrl(profile.getAvatarUrl());
        }

        return profileRepository.save(saved);
    }
}
