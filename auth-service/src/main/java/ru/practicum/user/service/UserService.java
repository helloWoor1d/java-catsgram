package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.NewUserRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final KeycloakAdminService keycloakService;
    private final KafkaProducer kafkaProducer;

    public void register(NewUserRequest user) {
        UserRepresentation representation = keycloakService.createUser(user);
        kafkaProducer.userRegister(representation.getId(), representation.getEmail(), representation.getUsername());
        log.info("User Service: user {} success registered", representation.getUsername());
    }

    public void logout(String id) {
        keycloakService.logout(id);
        log.info("User Service: user {} success logout", id);
    }

    public void delete(String id) {
        keycloakService.deleteUserById(id);
        kafkaProducer.userDelete(id);
        log.info("User Service: user {} success deleted", id);
    }
}
