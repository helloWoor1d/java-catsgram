package ru.practicum.follow.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.follow.model.Follow;
import ru.practicum.profile.model.ProfileShort;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Optional<Follow> findAllByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("SELECT p.id, p.login, p.avatarUrl " +
            "FROM Profile AS p " +
            "LEFT JOIN Follow AS f ON p.id = f.following.id " +
            "WHERE f.following.id = :id ")
    Page<ProfileShort> getFollowers(@Param("id") Long followingId, Pageable pageable);

    @Query("SELECT p.id, p.login, p.avatarUrl " +
            "FROM Profile AS p " +
            "LEFT JOIN Follow AS f " +
            "ON p.id = f.follower.id " +
            "WHERE f.follower.id = :id ")
    Page<ProfileShort> getFollowings(@Param("id") Long followerId, Pageable pageable);
}
