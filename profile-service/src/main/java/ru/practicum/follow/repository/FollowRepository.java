package ru.practicum.follow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.follow.model.Follow;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Optional<Follow> findAllByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
