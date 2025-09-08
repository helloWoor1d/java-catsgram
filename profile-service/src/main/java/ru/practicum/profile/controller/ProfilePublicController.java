package ru.practicum.profile.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.practicum.profile.dto.ProfileView;
import ru.practicum.profile.dto.UpdateProfileReq;
import ru.practicum.profile.dto.mapper.ProfileMapper;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.service.ProfileService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profiles")
public class ProfilePublicController {
    private final ProfileService profileService;
    private final FollowService followService;    private final ProfileMapper profileMapper;

    @GetMapping("/me")
    public ResponseEntity<ProfileView> getProfile(@AuthenticationPrincipal Jwt jwt) {
        String keycloakId = jwt.getSubject();

        return ResponseEntity.ok(profileMapper.toView(
                        profileService.getCurrentProfile(keycloakId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileView> getProfile(@PathVariable Long id) {  // toDo: открытый профиль -> всем
        return ResponseEntity.ok(profileMapper.toView(                     // ToDo: закрытый -> полный только одобренным подписчикам, основная инфа всем
                        profileService.getProfile(id)));
    }

    @PatchMapping
    public ResponseEntity<ProfileView> updateProfile(@AuthenticationPrincipal Jwt jwt,
                                                     @RequestBody @Valid UpdateProfileReq updateReq) {
        String keycloakId = jwt.getSubject();
        Profile profile = profileMapper.toProfile(updateReq, keycloakId);

        return ResponseEntity.ok(profileMapper.toView(
                        profileService.updateProfile(profile)));
    }

    @PostMapping("/{followingId}/follow")
    public ResponseEntity<Follow> followProfile(@AuthenticationPrincipal Jwt jwt,
                                                @PathVariable Long followingId) {
        Follow follow = followService.follow(jwt.getSubject(), followingId);
        return ResponseEntity.ok(follow);
    }

    @DeleteMapping("/{followingId}/unfollow")
    public void unfollowProfile(@AuthenticationPrincipal Jwt jwt,
                                @PathVariable Long followingId) {
        followService.unfollow(jwt.getSubject(), followingId);
    }
}