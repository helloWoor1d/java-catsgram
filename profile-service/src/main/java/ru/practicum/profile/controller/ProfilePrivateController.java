package ru.practicum.profile.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.follow.model.Follow;
import ru.practicum.follow.service.FollowService;
import ru.practicum.profile.dto.ProfileResponse;
import ru.practicum.profile.dto.UpdateProfileReq;
import ru.practicum.profile.dto.mapper.ProfileMapper;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.model.ProfileShort;
import ru.practicum.profile.service.ProfileService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profiles/me")
@Slf4j
public class ProfilePrivateController {
    private final ProfileService profileService;
    private final FollowService followService;

    private final ProfileMapper profileMapper;

    @GetMapping
    public ResponseEntity<ProfileResponse> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();

        return ResponseEntity.ok(profileMapper.toView(
                profileService.getCurrentProfile(keycloakId)));
    }

    @PatchMapping
    public ResponseEntity<ProfileResponse> updateMyProfile(@AuthenticationPrincipal Jwt jwt,
                                                           @RequestBody @Valid UpdateProfileReq updateReq) {
        String keycloakId = jwt.getSubject();
        Profile profile = profileMapper.toProfile(updateReq, keycloakId);

        return ResponseEntity.ok(profileMapper.toView(
                profileService.updateProfile(profile)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal Jwt jwt,
                                                      @PathVariable Long id) {
        Profile profile = profileService.getProfile(id);
        Profile current = profileService.getCurrentProfile(jwt.getSubject());

        Optional<Follow> follow = followService.getFollow(current.getId(), profile.getId());
        if (follow.isEmpty()) {
            return ResponseEntity.ok(profileMapper.toPrivateView(profile));
        }
        return ResponseEntity.ok(profileMapper.toView(profile));
    }

    @PostMapping("/follow/{followingId}")
    public ResponseEntity<Follow> followProfile(@AuthenticationPrincipal Jwt jwt,
                                                @PathVariable Long followingId) {
        Follow follow = followService.follow(jwt.getSubject(), followingId);
        return ResponseEntity.ok(follow);
    }

    @DeleteMapping("/follow/{followingId}")
    public void unfollowProfile(@AuthenticationPrincipal Jwt jwt,
                                @PathVariable Long followingId) {
        followService.unfollow(jwt.getSubject(), followingId);
    }

    @GetMapping("/followers")
    public List<ProfileShort> getMyFollowers(@AuthenticationPrincipal Jwt jwt) {
        return profileService.getFollowers(jwt.getSubject());
    }

    @GetMapping("/following")
    public List<ProfileShort> getMyFollowings(@AuthenticationPrincipal Jwt jwt) {
        return profileService.getFollowings(jwt.getSubject());
    }
}
