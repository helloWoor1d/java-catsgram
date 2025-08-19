package ru.practicum.user.service;

import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.NewUserRequest;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class KeycloakAdminService {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    public UserRepresentation createUser(NewUserRequest request) {
        log.debug("Creating new user request {}", request);
        UserRepresentation user = getUserRepresentation(request);

        UsersResource usersResource = getResource();
        Response response = usersResource.create(user);

        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            log.info("Created user {}", user.getUsername());
        } else if(response.getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
            log.info("Bad request {}", user.getUsername());
            throw new ValidationException("Ошибка валидации, выбранный вами логин уже существует");
        } else {
            log.info("Invalid request {}", user.getUsername());
        }

        String userId = CreatedResponseUtil.getCreatedId(response);
        user.setId(userId);
        setRoleToUser(userId);

        return user;
    }

    private UserRepresentation getUserRepresentation(NewUserRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setEmail(request.getEmail());
        user.setEmailVerified(true);            // toDo: подтвержедение почты
        user.setUsername(request.getUsername());
        user.setEnabled(true);                  // "включаем" пользователя

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        credential.setTemporary(false);         // пароль не временный

        user.setCredentials(List.of(credential));
        return user;
    }

    private void setRoleToUser(String userId) {
        String clientUuid = keycloak.realm(realm)
                .clients()
                .findByClientId(clientId)
                .get(0).getId();

        RoleRepresentation role = keycloak
                .realm(realm)
                .clients().get(clientUuid)
                .roles().get("catsgram.user")
                .toRepresentation();

        keycloak.realm(realm)
                .users().get(userId)
                .roles().clientLevel(clientUuid)
                .add(List.of(role));
    }

    public void deleteUserById(String id) {
        UsersResource usersResource = getResource();
        usersResource.delete(id);
    }

    public void logout(String userId) {
        UsersResource usersResource = getResource();
        usersResource.get(userId).logout();
    }

    private UsersResource getResource() {
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users();
    }
}
