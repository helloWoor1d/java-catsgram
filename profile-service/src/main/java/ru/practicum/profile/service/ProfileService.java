package ru.practicum.profile.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ProfileDeactivatedException;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.repository.ProfileRepository;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor

@Service
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public Profile getCurrentProfile(String authId) {
        log.debug("Get current profile authId: {}", authId);
        return profileRepository.findProfileByAuthProviderId(authId)
                .orElseThrow(() -> new EntityNotFoundException("No profile found for authId: " + authId));
    }

    @Transactional(readOnly = true)
    public Profile getProfile(Long profId) {
        log.debug("Get profile by id: {}", profId);
        Profile profile = profileRepository.findById(profId)
                .orElseThrow(() -> new EntityNotFoundException("No profile found for id: " + profId));

        if (profile.getDeactivated()) throw new ProfileDeactivatedException();
        return profile;
    }

    public Profile createProfile(Profile profile) {
        log.debug("Create profile for user with email {}", profile.getEmail());
        return profileRepository.save(profile);
    }

    public void deactivateProfile(Profile profile) {
        Profile saved = profileRepository.findProfileByAuthProviderId(profile.getAuthProviderId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Profile not found"));

        if(saved.getDeactivated()) return;

        saved.setDeactivated(true);
        saved.setDeactivatedAt(LocalDateTime.now());
        profileRepository.save(saved);
        log.debug("Profile deactivated {}", profile.getId());
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
