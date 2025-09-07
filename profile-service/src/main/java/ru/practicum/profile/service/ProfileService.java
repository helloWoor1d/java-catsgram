package ru.practicum.profile.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.profile.event.UserDeletedEvent;
import ru.practicum.profile.event.UserRegisteredEvent;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.repository.ProfileRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    public Profile getCurrentProfile(String keycloakId) {
        log.debug("Get current profile keycloakId: {}", keycloakId);
        return profileRepository.findProfileByAuthProviderId(keycloakId)
                .orElseThrow(() -> new EntityNotFoundException("No profile found for keycloakId: " + keycloakId));
    }

    public Profile getProfile(Long postId) {
        log.debug("Get profile by id: {}", postId);
        return profileRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("No profile found for id: " + postId));
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

    public Map<Long, Profile> getProfilesByIds(List<Long> ids) {
        log.debug("Get profiles by ids: {}", ids);
        return profileRepository
                .findAllByIdIn(ids).stream()
                .collect(Collectors.toMap(Profile::getId, profile -> profile));
    }
}
