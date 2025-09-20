package ru.practicum.profile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.AccessDeniedForProfileException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.exception.ProfileDeactivatedException;
import ru.practicum.follow.model.Follow;
import ru.practicum.follow.service.FollowService;
import ru.practicum.profile.model.domain.Profile;
import ru.practicum.profile.model.domain.ProfileShort;
import ru.practicum.profile.model.domain.ProfileView;
import ru.practicum.profile.model.domain.mapper.ProfileModelMapper;
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
    private final ProfileModelMapper profileMapper;

    @Transactional(readOnly = true)
    public Profile getCurrentProfile(String authId) {
        log.debug("Get current profile authId: {}", authId);
        return getProfileByAuthId(authId);
    }

    @Transactional(readOnly = true)
    public ProfileView getCurrentProfileView(String authId) {
        log.info("Get current profile view for authId: {}", authId);
        Profile profile = getProfileByAuthId(authId);
        return buildProfileView(profile, profile);
    }

    @Transactional(readOnly = true)
    public ProfileView getProfileForViewer(Profile viewer, Long profileId) {
        log.info("Get profile {} for viewer: {}", profileId, viewer.getId());
        Profile profile = getProfileById(profileId);

        if (profile.getDeactivated()) throw new ProfileDeactivatedException("Profile deactivated");
        return buildProfileView(viewer, profile);
    }

    public Profile createProfile(Profile profile) {
        log.info("Create profile for user with email {}", profile.getEmail());
        return profileRepository.save(profile);
    }

    public void deactivateProfile(Profile profile) {
        Profile saved = getProfileByAuthId(profile.getAuthProviderId());
        if (saved.getDeactivated()) return;

        saved.setDeactivated(true);
        saved.setDeactivatedAt(LocalDateTime.now());
        profileRepository.save(saved);
        log.info("Profile deactivated {}", profile.getId());
    }

    public ProfileView updateProfile(Profile profile) {
        log.debug("Update profile {}", profile.getAuthProviderId());
        Profile saved = getProfileByAuthId(profile.getAuthProviderId());

        profileMapper.updateProfileFromUpdateRequest(saved, profile);
        profile = profileRepository.save(saved);
        return buildProfileView(profile, profile);
    }

    @Transactional(readOnly = true)
    public Page<ProfileShort> getFollowers(Profile viewer, Long followingId,
                                           Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        if (viewer.getId().equals(followingId)) {
            log.debug("Profile {} get its followers", viewer.getId());
            return followService.getFollowers(viewer.getId(), pageable);
        }
        Profile profile = getProfileById(followingId);
        if (!profile.getPrivateProfile()) {
            log.debug("Get followers for public profile {} by {}", followingId, viewer.getId());
            return followService.getFollowers(followingId, pageable);
        }
        return getPrivateProfFollowers(viewer.getId(), followingId, pageable);
    }

    private Page<ProfileShort> getPrivateProfFollowers(Long viewerId, Long followingId,
                                                       Pageable pageable) {
        log.debug("Get followers for private profile");
        if (followService.followExists(viewerId, followingId)) {
            return followService.getFollowers(followingId, pageable);
        }
        throw new AccessDeniedForProfileException("Приватный профиль, нельзя получить данные");
    }

    @Transactional(readOnly = true)
    public Page<ProfileShort> getFollowings(Profile viewer, Long followerId,
                                            Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        if (viewer.getId().equals(followerId)) {
            log.debug("Profile {} get its followings", viewer.getId());
            return followService.getFollowings(viewer.getId(), pageable);
        }
        Profile profile = getProfileById(followerId);
        if (!profile.getPrivateProfile()) {
            log.debug("Get followings for public profile {} by {}", followerId, viewer.getId());
            return followService.getFollowings(followerId, pageable);
        }
        return getPrivateProfFollowings(viewer.getId(), followerId, pageable);
    }

    private Page<ProfileShort> getPrivateProfFollowings(Long viewerId, Long followerId,
                                                        Pageable page) {
        log.debug("Get followings for private profile {} by {}", followerId, viewerId);
        if (followService.followExists(viewerId, followerId)) {
            return followService.getFollowings(followerId, page);
        }
        throw new AccessDeniedForProfileException("Приватный профиль, нельзя получить данные");
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
        Map<Long, Profile> result = profiles.stream()
                .collect(Collectors.toMap(Profile::getId, p -> p));
        if (profiles.size() < ids.size()) {
            List<Long> missing = ids.stream().filter(id -> !result.containsKey(id)).toList();
            throw new DataNotFoundException("Not found profile for id " + missing);
        }
        return result;
    }

    private Profile getProfileById(Long id) {
        log.debug("Get profile by id {}", id);
        return profileRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No profile found for id: " + id));
    }

    private Profile getProfileByAuthId(String authId) {
        log.debug("Get profile by authId: {}", authId);
        return profileRepository.findProfileByAuthProviderId(authId)
                .orElseThrow(() -> new DataNotFoundException("No profile found for authId: " + authId));
    }

    private ProfileView buildProfileView(Profile viewer, Profile profile) {
        boolean followExist = followService.followExists(viewer.getId(), profile.getId());
        boolean canSeeFull = viewer.getId().equals(profile.getId()) || !profile.getPrivateProfile() || followExist;
        ProfileView profileView = ProfileView.builder()
                .id(profile.getId())
                .bio(profile.getBio())
                .login(profile.getLogin())
                .avatarUrl(profile.getAvatarUrl())
                .isPrivate(profile.getPrivateProfile())
                .isFollowing(followExist)
                .build();
        if (canSeeFull) {
            profileView.setPosts(List.of());
        }
        return profileView;
    }
}
