package ru.practicum.profile.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.profile.dto.PrivateProfileView;
import ru.practicum.profile.dto.ProfileView;
import ru.practicum.profile.dto.UpdateProfileReq;
import ru.practicum.profile.model.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileView toView(Profile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authProviderId", source = "authId")
    @Mapping(target = "deactivated", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "deactivatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Profile toProfile(UpdateProfileReq updateProfileReq, String authId);

    PrivateProfileView toPrivateView(Profile profile);
}
