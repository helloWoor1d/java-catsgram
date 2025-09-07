package ru.practicum.follow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.follow.model.Follow;
import ru.practicum.follow.repository.FollowRepository;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.service.ProfileService;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class FollowService {
    private final ProfileService profileService;
    private final FollowRepository followRepository;

    public Follow follow(String currentProfId, Long followingId) {
        Profile current = profileService.getCurrentProfile(currentProfId);
        Profile following = profileService.getProfile(followingId);

        Follow follow = Follow.builder()
                .follower(current)
                .following(following)
                .status(false)
                .followedAt(LocalDateTime.now())
                .build();
        log.debug("{} followed {}", current.getId(), following.getId());
        return followRepository.save(follow);
    }

    public void unfollow(String currentProfId, Long followingId) {
        Profile current = profileService.getCurrentProfile(currentProfId);

        log.debug("{} unfollowed {}", current.getId(), followingId);
        followRepository.deleteByFollowerIdAndFollowingId(current.getId(), followingId);
    }
}
