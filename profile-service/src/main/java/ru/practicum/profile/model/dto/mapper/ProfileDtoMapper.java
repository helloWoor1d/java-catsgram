package ru.practicum.profile.model.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import ru.practicum.profile.model.domain.Profile;
import ru.practicum.profile.model.domain.ProfileShort;
import ru.practicum.profile.model.domain.ProfileView;
import ru.practicum.profile.model.dto.ProfileShortDto;
import ru.practicum.profile.model.dto.ProfileViewDto;
import ru.practicum.profile.model.dto.UpdateProfileReq;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileDtoMapper {
    ProfileViewDto toView(ProfileView profileView);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "deactivatedAt", ignore = true)
    @Mapping(target = "deactivated", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "authProviderId", source = "authId")
    Profile toProfile(UpdateProfileReq updateProfileReq, String authId);

    ProfileShortDto toShortDto(ProfileShort profileShort);
}
