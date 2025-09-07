package ru.practicum.profile.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(generator = "id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
            name = "id_seq",
            sequenceName = "profiles_id_seq",
            allocationSize = 15)
    private Long id;

    @Column(name = "auth_id")
    private String authProviderId;

    private String email;

    private String login;

    private String bio;

    @Column(name = "avatar")
    private String avatarUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deactivated")
    @Builder.Default
    private Boolean deactivated = Boolean.FALSE;

    @Column(name = "deactivated_at")
    private LocalDateTime deactivatedAt;
}
