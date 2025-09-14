package ru.practicum.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class UpdateProfileReq {
    private String login;

    private String bio;

    private String avatarUrl;

    @JsonProperty("private")
    private Boolean privateProfile;
}
