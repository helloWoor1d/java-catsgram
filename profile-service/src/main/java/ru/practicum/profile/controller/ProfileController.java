package ru.practicum.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.follow.model.Follow;
import ru.practicum.profile.dto.ProfileShortDto;
import ru.practicum.profile.dto.ProfileViewDto;
import ru.practicum.profile.dto.mapper.ProfileMapper;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.service.ProfileService;


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
    public Page<ProfileShortDto> getFollowers(@AuthenticationPrincipal Jwt jwt,
                                              @PathVariable Long profileId,
                                              @RequestParam(required = false, defaultValue = "0") Integer page,
                                              @RequestParam(required = false, defaultValue = "15") Integer size) {
        Profile viewer = profileService.getCurrentProfile(jwt.getSubject());
        return profileService.getFollowers(viewer, profileId, page, size)
                .map(profileMapper::toShortDto);
    }

    @GetMapping("/{profileId}/following")
    public Page<ProfileShortDto> getFollowings(@AuthenticationPrincipal Jwt jwt,
                                               @PathVariable Long profileId,
                                               @RequestParam(required = false, defaultValue = "0") Integer page,
                                               @RequestParam(required = false, defaultValue = "15") Integer size) {
        Profile viewer = profileService.getCurrentProfile(jwt.getSubject());
        return profileService.getFollowings(viewer, profileId, page, size)
                .map(profileMapper::toShortDto);
    }
}
