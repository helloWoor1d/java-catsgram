package ru.practicum.service;

import ru.practicum.event.UserDeletedEvent;
import ru.practicum.event.UserRegisteredEvent;
import ru.practicum.model.Profile;

public interface ProfileService {
    Profile getCurrentProfile(String id);

    Profile getProfile(Long id);

    void createProfile(UserRegisteredEvent event);

    void deactivateProfile(UserDeletedEvent event);

    Profile updateProfile(Profile profile);
}
