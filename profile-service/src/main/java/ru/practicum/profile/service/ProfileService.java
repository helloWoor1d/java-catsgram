package ru.practicum.profile.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ProfileDeactivatedException;
import ru.practicum.follow.model.Follow;
import ru.practicum.follow.service.FollowService;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.model.ProfileShort;
import ru.practicum.profile.model.ProfileView;
import ru.practicum.profile.repository.ProfileRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final FollowService followService;

    @Transactional(readOnly = true)
    public Profile getCurrentProfile(String authId) {
        log.debug("Get current profile authId: {}", authId);
        return getProfileByAuthId(authId);
    }

    @Transactional(readOnly = true)
    public ProfileView getProfileForViewer(Profile viewer, Long profileId) {
        log.debug("Get profile {} for viewer: {}", profileId, viewer.getId());
        Profile profile = getProfileById(profileId);

        if (profile.getDeactivated()) throw new ProfileDeactivatedException();

        boolean followExist = followService.followExists(viewer.getId(), profileId);
        boolean canSeeFull = !profile.getPrivateProfile() || followExist;

        ProfileView profileView = ProfileView.builder()
                .id(profile.getId())
                .bio(profile.getBio())
                .login(profile.getLogin())
                .avatarUrl(profile.getAvatarUrl())
                .postsCount(null)
                .followingCount(null)
                .followersCount(null)
                .isPrivate(profile.getPrivateProfile())
                .isFollowing(followExist)
                .build();
        if (canSeeFull) {
            // toDo: заполняем посты
        }
        return profileView;
    }

    public Profile createProfile(Profile profile) {
        log.debug("Create profile for user with email {}", profile.getEmail());
        return profileRepository.save(profile);
    }

    public void deactivateProfile(Profile profile) {
        Profile saved = getProfileByAuthId(profile.getAuthProviderId());
        if (saved.getDeactivated()) return;

        saved.setDeactivated(true);
        saved.setDeactivatedAt(LocalDateTime.now());
        profileRepository.save(saved);
        log.debug("Profile deactivated {}", profile.getId());
    }

    public Profile updateProfile(Profile profile) {
        log.debug("Update profile {}", profile.getAuthProviderId());
        Profile saved = getProfileByAuthId(profile.getAuthProviderId());

        if (profile.getLogin() != null && !profile.getLogin().isBlank()) {         // toDo: mapstruct
            saved.setLogin(profile.getLogin());
        }
        if (profile.getBio() != null && !profile.getBio().isBlank()) {
            saved.setBio(profile.getBio());
        }
        if (profile.getAvatarUrl() != null && !profile.getEmail().isBlank()) {
            saved.setAvatarUrl(profile.getAvatarUrl());
        }
        if (profile.getPrivateProfile() != null) {
            saved.setPrivateProfile(profile.getPrivateProfile());
        }

        return profileRepository.save(saved);
    }

    @Transactional(readOnly = true)
    public List<ProfileShort> getFollowers(Profile viewer, Long followingId) {
        if (viewer.getId().equals(followingId)) {
            log.debug("Profile {} get its followers", viewer.getId());
            return followService.getFollowers(viewer.getId());
        }
        Profile profile = getProfileById(followingId);
        if (!profile.getPrivateProfile()) {
            log.debug("Get followers for public profile {} by {}", followingId, viewer.getId());
            return followService.getFollowers(followingId);
        }
        return getPrivateProfFollowers(viewer.getId(), followingId);
    }

    private List<ProfileShort> getPrivateProfFollowers(Long viewerId, Long followingId) {
        log.debug("Get followers for private profile");
        if (followService.followExists(viewerId, followingId)) {
            return followService.getFollowers(followingId);
        }
        throw new RuntimeException("Доступ запрещен: приватный профиль, нельзя увидеть данные");
    }

    @Transactional(readOnly = true)
    public List<ProfileShort> getFollowings(Profile viewer, Long followerId) {
        if (viewer.getId().equals(followerId)) {
            log.debug("Profile {} get its followings", viewer.getId());
            return followService.getFollowings(viewer.getId());
        }
        Profile profile = getProfileById(followerId);
        if (!profile.getPrivateProfile()) {
            log.debug("Get followings for public profile {} by {}", followerId, viewer.getId());
            return followService.getFollowings(followerId);
        }
        return getPrivateProfFollowings(viewer.getId(), followerId);
    }

    private List<ProfileShort> getPrivateProfFollowings(Long viewerId, Long followerId) {
        log.debug("Get followings for private profile {} by {}", followerId, viewerId);
        if (followService.followExists(viewerId, followerId)) {
            return followService.getFollowings(followerId);
        }
        throw new RuntimeException("Доступ запрещен: приватный профиль, нельзя увидеть данные");
    }

    public Follow follow(Long followerId, Long followingId) {
        Map<Long, Profile> profiles = getProfilesByIds(List.of(followerId, followingId));
        log.debug("{} follow {}", followerId, followingId);
        return followService.follow(profiles.get(followerId), profiles.get(followingId));
    }

    public void unfollow(Long followerId, Long followingId) {
        Map<Long, Profile> profiles = getProfilesByIds(List.of(followerId, followingId));
        log.debug("{} unfollow {}", followerId, followingId);
        followService.unfollow(profiles.get(followerId), profiles.get(followingId));
    }

    private Map<Long, Profile> getProfilesByIds(List<Long> ids) {
        log.debug("Get profiles by ids {}", ids);
        List<Profile> profiles = profileRepository.findAllByIdIn(ids);
        return profiles.stream()
                .collect(Collectors.toMap(Profile::getId, p -> p));
    }

    private Profile getProfileById(Long id) {
        log.debug("Get profile by id {}", id);
        return profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No profile found for id: " + id));
    }

    private Profile getProfileByAuthId(String authId) {
        log.debug("Get profile by authId: {}", authId);
        return profileRepository.findProfileByAuthProviderId(authId)
                .orElseThrow(() -> new EntityNotFoundException("No profile found for authId: " + authId));
    }
}
