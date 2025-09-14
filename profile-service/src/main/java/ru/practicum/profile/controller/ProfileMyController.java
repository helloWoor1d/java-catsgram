package ru.practicum.profile.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.profile.dto.ProfileViewDto;
import ru.practicum.profile.dto.UpdateProfileReq;
import ru.practicum.profile.dto.mapper.ProfileMapper;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.service.ProfileService;


@RestController
@RequestMapping("/api/v1/profiles/me")
@RequiredArgsConstructor
public class ProfileMyController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @GetMapping
    public ResponseEntity<ProfileViewDto> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        String authId = jwt.getSubject();

        return ResponseEntity.ok(profileMapper.toView(
                profileService.getCurrentProfile(authId)));
    }

    @PatchMapping
    public ResponseEntity<ProfileViewDto> updateMyProfile(@AuthenticationPrincipal Jwt jwt,
                                                           @RequestBody @Valid UpdateProfileReq updateReq) {
        String authId = jwt.getSubject();
        Profile profile = profileMapper.toProfile(updateReq, authId);

        return ResponseEntity.ok(profileMapper.toView(
                profileService.updateProfile(profile)));
    }
}
