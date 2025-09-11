package ru.practicum.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class PrivateProfileView implements ProfileResponse {
    private Long id;

    private String login;

    private String bio;

    private String avatarUrl;

    @Builder.Default
    private Integer postCount = 0;

    @Builder.Default
    private Integer followersCount = 0;

    @Builder.Default
    private Integer followingCount = 0;

    private Boolean privateProfile;
}
