package ru.practicum.model;

//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

//@Entity
public class Profile {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE) //toDo: настроить Sequence
    private long id;

    private String keycloakId;

    private String email;

    private String username;

    private String bio;

    private String avatarUrl;
}
