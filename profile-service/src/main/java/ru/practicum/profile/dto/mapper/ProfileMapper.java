package ru.practicum.profile.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.profile.dto.ProfileShortDto;
import ru.practicum.profile.dto.ProfileViewDto;
import ru.practicum.profile.dto.UpdateProfileReq;
import ru.practicum.profile.model.Profile;
import ru.practicum.profile.model.ProfileShort;
import ru.practicum.profile.model.ProfileView;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileViewDto toView(Profile profile);

    ProfileViewDto toView(ProfileView profileView);

    List<ProfileViewDto> toViews(List<Profile> profiles);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authProviderId", source = "authId")
    @Mapping(target = "deactivated", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "deactivatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Profile toProfile(UpdateProfileReq updateProfileReq, String authId);

    ProfileShortDto toShortDto(ProfileShort profileShort);

    List<ProfileShortDto> toShortDto(List<ProfileShort> profileShort);
}
