package com.timesheetsystem.service.mapper;

import com.timesheetsystem.domain.User;
import com.timesheetsystem.domain.UserPreference;
import com.timesheetsystem.service.dto.UserDTO;
import com.timesheetsystem.service.dto.UserPreferenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserPreference} and its DTO {@link UserPreferenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserPreferenceMapper extends EntityMapper<UserPreferenceDTO, UserPreference> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    UserPreferenceDTO toDto(UserPreference s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
