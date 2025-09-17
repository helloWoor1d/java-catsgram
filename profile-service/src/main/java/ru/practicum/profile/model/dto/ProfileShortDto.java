package ru.practicum.profile.model.dto;

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

public class ProfileShortDto {
    private Long id;
    private String login;
    private String avatarUrl;
}
