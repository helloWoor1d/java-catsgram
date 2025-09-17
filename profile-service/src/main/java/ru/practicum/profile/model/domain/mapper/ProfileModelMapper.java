package ru.practicum.profile.model.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.profile.model.domain.Profile;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileModelMapper {
    void updateProfileFromUpdateRequest(@MappingTarget Profile saved, Profile updated);
}
