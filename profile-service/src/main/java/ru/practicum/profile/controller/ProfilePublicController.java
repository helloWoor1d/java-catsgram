package ru.practicum.profile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.follow.service.FollowService;
import ru.practicum.profile.dto.ProfileResponse;
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

    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long profileId) {
        Profile profile = profileService.getProfile(profileId);
        if (profile.getPrivateProfile()) return ResponseEntity.ok(
                profileMapper.toPrivateView(profile));
        return ResponseEntity.ok(profileMapper.toView(profile));
    }

    @GetMapping("/{profileId}/followers")
    public List<Profile> getProfileFollowers(@PathVariable Long profileId) {
        return null;
    }

    @GetMapping("/{profileId}/following")
    public List<Profile> getProfileFollowing(@PathVariable Long profileId) {
        return null;
    }
}