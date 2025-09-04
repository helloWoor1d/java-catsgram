package ru.practicum.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.ProfileView;
import ru.practicum.dto.UpdateProfileReq;
import ru.practicum.model.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileView toView(Profile profileView);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authProviderId", source = "authId")
    @Mapping(target = "deactivated", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "deactivatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Profile toProfile(UpdateProfileReq updateProfileReq, String authId);
}
