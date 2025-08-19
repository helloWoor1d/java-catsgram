package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class NewUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Email(regexp = ".+[@].+[\\.].+")
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;
}
