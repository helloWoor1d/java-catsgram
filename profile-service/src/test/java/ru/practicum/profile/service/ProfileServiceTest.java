package ru.practicum.profile.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.repository.ProfileRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;

@ActiveProfiles(profiles = "test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)

@DataJpaTest
@Import(ProfileService.class)
public class ProfileServiceTest {
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private Profile prof1;

    @BeforeEach
    public void setUp() {
        prof1 = Profile.builder()
                .authProviderId("auth-provider-id")
                .login("login")
                .email("email@email.com")
                .createdAt(LocalDateTime.now())
                .build();
    }

//    @Test
//    public void whenCreateUpdateOrGetProfile_thenSuccess() {
//        Profile created = profileService.createProfile(prof1);
//
//        assertThat(created.getId(), notNullValue());
//        assertThat(created, samePropertyValuesAs(created));
//
//        created.setBio("prof1 bio");
//        created.setLogin("updated login");
//        profileService.updateProfile(created);
//
//        Profile updated  = profileService.getProfile(created.getId());
//        assertThat(created, samePropertyValuesAs(updated));
//    }

    @Test
    public void whenGetCurrentProfile_thenSuccess() {
        Profile created = profileRepository.save(prof1);

        Profile saved = profileService.getCurrentProfile(prof1.getAuthProviderId());
        assertThat(created, samePropertyValuesAs(saved));
    }

    @Test
    public void whenDeactivateProfile_thenSuccess() {
        Profile created = profileRepository.save(prof1);

        created.setDeactivated(true);
        created.setDeactivatedAt(LocalDateTime.now());
        profileService.deactivateProfile(created);

        Profile deactivated = profileRepository.findById(created.getId()).orElseThrow();
        assertThat(deactivated, samePropertyValuesAs(created));
        assertThat(deactivated.getDeactivated(), is(true));
    }
}
