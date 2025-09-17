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
import ru.practicum.profile.model.dto.ProfileViewDto;
import ru.practicum.profile.model.dto.UpdateProfileReq;
import ru.practicum.profile.model.dto.mapper.ProfileDtoMapper;
import ru.practicum.profile.model.domain.Profile;
import ru.practicum.profile.service.ProfileService;


@RestController
@RequestMapping("/api/v1/profiles/me")
@RequiredArgsConstructor
public class ProfileMyController {
    private final ProfileService profileService;
    private final ProfileDtoMapper dtoMapper;

    @GetMapping
    public ResponseEntity<ProfileViewDto> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        String authId = jwt.getSubject();

        return ResponseEntity.ok(dtoMapper.toView(
                profileService.getCurrentProfileView(authId)));
    }

    @PatchMapping
    public ResponseEntity<ProfileViewDto> updateMyProfile(@AuthenticationPrincipal Jwt jwt,
                                                          @RequestBody @Valid UpdateProfileReq updateReq) {
        String authId = jwt.getSubject();
        Profile profile = dtoMapper.toProfile(updateReq, authId);

        return ResponseEntity.ok(dtoMapper.toView(
                profileService.updateProfile(profile)));
    }
}
