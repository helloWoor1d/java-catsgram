package ru.practicum.profile.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
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
import ru.practicum.profile.model.domain.Profile;
import ru.practicum.profile.model.dto.ProfileShortDto;
import ru.practicum.profile.model.dto.ProfileViewDto;
import ru.practicum.profile.model.dto.mapper.ProfileDtoMapper;
import ru.practicum.profile.service.ProfileService;


@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@EnableSpringDataWebSupport(
        pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileDtoMapper dtoMapper;

    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileViewDto> getProfile(@AuthenticationPrincipal Jwt jwt,
                                                     @PathVariable Long profileId) {
        Profile viewer = profileService.getCurrentProfile(jwt.getSubject());
        return ResponseEntity.ok(dtoMapper.toView(
                profileService.getProfileForViewer(viewer, profileId)));
    }

    @PostMapping("/{profileId}/follow")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Follow> followProfile(@AuthenticationPrincipal Jwt jwt,
                                                @PathVariable Long profileId) {
        Profile viewer = profileService.getCurrentProfile(jwt.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                profileService.follow(viewer.getId(), profileId));
    }

    @DeleteMapping("/{profileId}/follow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollowProfile(@AuthenticationPrincipal Jwt jwt,
                                @PathVariable Long profileId) {
        Profile viewer = profileService.getCurrentProfile(jwt.getSubject());
        profileService.unfollow(viewer.getId(), profileId);
    }

    @GetMapping("/{profileId}/followers")
    public Page<ProfileShortDto> getFollowers(@AuthenticationPrincipal Jwt jwt,
                                              @PathVariable Long profileId,
                                              @Min (0) @Max (100) @RequestParam(required = false, defaultValue = "0") Integer page,
                                              @Min (0) @Max (100) @RequestParam(required = false, defaultValue = "15") Integer size) {
        Profile viewer = profileService.getCurrentProfile(jwt.getSubject());
        return profileService.getFollowers(viewer, profileId, page, size)
                .map(dtoMapper::toShortDto);
    }

    @GetMapping("/{profileId}/following")
    public Page<ProfileShortDto> getFollowings(@AuthenticationPrincipal Jwt jwt,
                                               @PathVariable Long profileId,
                                               @Min (0) @Max (100) @RequestParam(required = false, defaultValue = "0") Integer page,
                                               @Min (0) @Max (100) @RequestParam(required = false, defaultValue = "15") Integer size) {
        Profile viewer = profileService.getCurrentProfile(jwt.getSubject());
        return profileService.getFollowings(viewer, profileId, page, size)
                .map(dtoMapper::toShortDto);
    }
}
