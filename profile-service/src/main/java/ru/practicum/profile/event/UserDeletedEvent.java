package ru.practicum.profile.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDeletedEvent {
    private String userId;
    private LocalDateTime deletedAt;
}
