package ru.practicum.profile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.profile.model.Profile;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findProfileByAuthProviderId(String authId);

    List<Profile> findAllByIdIn(List<Long> ids);
}
