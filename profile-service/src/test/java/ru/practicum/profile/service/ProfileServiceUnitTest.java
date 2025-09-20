package ru.practicum.profile.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.profile.model.domain.Profile;
import ru.practicum.profile.repository.ProfileRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProfileServiceUnitTest {
    @InjectMocks
    private ProfileService profileService;

    @Mock
    private ProfileRepository profileRepository;

    private Profile prof1;

    @BeforeEach
    public void setUp() {
        prof1 = Profile.builder()
                .id(1L)
                .email("email@email.com")
                .login("login")
                .build();
    }

//    @Test
//    public void getProfile_whenProfileNotFount_thenThrowException() {
//        when(profileRepository.findById(anyLong()))
//                .thenReturn(Optional.empty());
//
//        Exception ex = assertThrows(EntityNotFoundException.class, () -> profileService.getProfile(prof1.getId()));
//        assertThat(ex.getMessage(), is("No profile found for id: 1"));
//    }
//
//    @Test
//    public void getProfile_thenSuccess() {
//        when(profileRepository.findById(anyLong()))
//                .thenReturn(Optional.of(prof1));
//
//        Profile profile = profileService.getProfile(prof1.getId());
//
//        Mockito.verify(profileRepository, Mockito.times(1)).findById(prof1.getId());
//        assertThat(profile.getId(), is(prof1.getId()));
//        assertThat(profile.getLogin(), is(prof1.getLogin()));
//        assertThat(profile.getEmail(), is(prof1.getEmail()));
//    }
//
//    @Test
//    public void getProfile_whenProfileIsDeactivated_thenThrowException() {
//        prof1.setDeactivated(true);
//        when(profileRepository.findById(anyLong())).thenReturn(
//                Optional.of(prof1));
//
//        Exception ex = assertThrows(ProfileDeactivatedException.class,
//                () -> profileService.getProfile(prof1.getId()));
//        assertThat(ex.getMessage(), is("Profile deactivated"));
//        Mockito.verify(profileRepository, Mockito.times(1)).findById(prof1.getId());
//    }
}
