package ru.practicum.follow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.profile.model.Profile;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name =  "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "id_seq")
    @SequenceGenerator(name = "id_seq",
            sequenceName = "follows_id_seq",
            allocationSize = 15)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    @ToString.Exclude
    private Profile follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    @ToString.Exclude
    private Profile following;

    private Boolean status;

    private LocalDateTime followedAt;
}
