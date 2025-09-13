package ru.practicum.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.follow.model.Follow;
import ru.practicum.profile.dto.ProfileShortDto;
import ru.practicum.profile.dto.ProfileViewDto;
import ru.practicum.profile.dto.mapper.ProfileMapper;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.service.ProfileService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileViewDto> getProfile(@AuthenticationPrincipal Jwt jwt,
                                                      @PathVariable Long profileId) {
        Profile viewer = profileService.getCurrentProfile(jwt.getSubject());
        return ResponseEntity.ok(profileMapper.toView(
                profileService.getProfileForViewer(viewer, profileId)));
    }

    @PostMapping("/{profileId}/follow")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Follow> followProfile(@AuthenticationPrincipal Jwt jwt,
                                                @PathVariable Long profileId) {
        Profile viewer = profileService.getCurrentProfile(jwt.getSubject());
        return ResponseEntity.ok(
                profileService.follow(viewer.getId(), profileId));
    }

    @DeleteMapping("/{profileId}/unfollow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollowProfile(@AuthenticationPrincipal Jwt jwt,
                                @PathVariable Long profileId) {
        Profile viewer = profileService.getCurrentProfile(jwt.getSubject());
        profileService.unfollow(viewer.getId(), profileId);
    }

    @GetMapping("/{profileId}/followers")
    public List<ProfileShortDto> getFollowers(@AuthenticationPrincipal Jwt jwt,     //toDo: пагинация
                                              @PathVariable Long profileId) {
        Profile viewer = profileService.getCurrentProfile(jwt.getSubject());
        return profileMapper.toShortDto(
                profileService.getFollowers(viewer, profileId));
    }

    @GetMapping("/{profileId}/following")
    public List<ProfileShortDto> getFollowings(@AuthenticationPrincipal Jwt jwt,
                                              @PathVariable Long profileId) {
        Profile viewer = profileService.getCurrentProfile(jwt.getSubject());
        return profileMapper.toShortDto(profileService.getFollowings(viewer, profileId));
    }
}
