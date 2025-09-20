package ru.practicum.follow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.follow.model.Follow;
import ru.practicum.follow.repository.FollowRepository;
import ru.practicum.profile.model.domain.Profile;
import ru.practicum.profile.model.domain.ProfileShort;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class FollowService {
    private final FollowRepository followRepository;

    public Follow follow(Profile follower, Profile following) {
        if (followExists(follower.getId(), following.getId())) {
            throw new DataIntegrityViolationException("Follower already exists");
        }
        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .status(false)
                .followedAt(LocalDateTime.now())
                .build();
        log.debug("{} followed {}", follower.getId(), following.getId());
        return followRepository.save(follow);
    }

    public void unfollow(Profile follower, Profile following) {
        if (!followExists(follower.getId(), following.getId())) {
            throw new DataIntegrityViolationException("Follower does not exist");
        }
        log.debug("{} unfollowed {}", follower.getId(), following);
        followRepository.deleteByFollowerIdAndFollowingId(follower.getId(), following.getId());
    }

    public Optional<Follow> getFollow(Long followerId, Long followingId) {
        log.debug("Get follow - followerId: {}, followingId: {}", followerId, followingId);
        return followRepository.findAllByFollowerIdAndFollowingId(followerId, followingId);
    }

    public Page<ProfileShort> getFollowers(Long followingId, Pageable page) {
        log.debug("Get followers - for: {}", followingId);
        return followRepository.getFollowers(followingId, page);
    }

    public Page<ProfileShort> getFollowings(Long followerId, Pageable page){
        log.debug("Get followings - for: {}", followerId);
        return followRepository.getFollowings(followerId, page);
    }

    public boolean followExists(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }
}
