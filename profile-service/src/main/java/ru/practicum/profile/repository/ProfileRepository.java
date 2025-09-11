package ru.practicum.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.model.ProfileShort;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findProfileByAuthProviderId(String authId);

    List<Profile> findAllByIdIn(List<Long> ids);

    @Query("SELECT p.id AS id, p.login AS login, p.avatarUrl AS avatarUrl " +
            "FROM Profile AS p " +
            "LEFT JOIN Follow AS f ON f.follower.id = p.id " +
            "WHERE f.following.id = :id ")
    List<ProfileShort> getFollowers(@Param("id") Long profileId);

    @Query("SELECT p.id AS id, p.login AS login, p.avatarUrl AS avatarUrl " +
            "FROM Profile AS p " +
            "LEFT JOIN Follow AS f ON f.follower.id = p.id " +
            "WHERE f.follower.id = :id ")
    List<ProfileShort> getFollowings(@Param("id") Long profileId);
}
