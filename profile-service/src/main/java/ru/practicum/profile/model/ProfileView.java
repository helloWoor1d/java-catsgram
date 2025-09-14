package ru.practicum.profile.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class ProfileView {
    private Long id;

    private String login;

    private String bio;

    private String avatarUrl;

    @Builder.Default
    private Integer postsCount = 0;

    @Builder.Default
    private Integer followersCount = 0;

    @Builder.Default
    private Integer followingCount = 0;

    @Builder.Default
    private List<String> posts = null;

    private Boolean isFollowing;

    private Boolean isPrivate;
}
