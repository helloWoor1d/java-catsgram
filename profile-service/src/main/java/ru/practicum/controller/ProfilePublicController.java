package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.ProfileView;
import ru.practicum.dto.UpdateProfileReq;
import ru.practicum.dto.mapper.ProfileMapper;
import ru.practicum.model.Profile;
import ru.practicum.service.ProfileService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profiles")
public class ProfilePublicController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @GetMapping("/me")
    public ResponseEntity<ProfileView> getProfile(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        String keycloakId = jwt.getSubject();

        return ResponseEntity.ok(
                profileMapper.toView(
                        profileService.getCurrentProfile(keycloakId)));
    }

    @GetMapping("/{login}")
    public ResponseEntity<ProfileView> getProfile(@PathVariable String login) {
        return ResponseEntity.ok(
                profileMapper.toView(
                        profileService.getProfile(login)));
    }

    @PatchMapping
    public ResponseEntity<ProfileView> updateProfile(Authentication authentication,
                                              @RequestBody @Valid UpdateProfileReq updateReq) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String keycloakId = jwt.getSubject();

        Profile profile = profileMapper.toProfile(updateReq, keycloakId);

        return ResponseEntity.ok(
                profileMapper.toView(
                        profileService.updateProfile(profile)));
    }
}