package com.timesheetsystem.service.mapper;

import com.timesheetsystem.domain.TimesheetAlert;
import com.timesheetsystem.domain.User;
import com.timesheetsystem.service.dto.TimesheetAlertDTO;
import com.timesheetsystem.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimesheetAlert} and its DTO {@link TimesheetAlertDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimesheetAlertMapper extends EntityMapper<TimesheetAlertDTO, TimesheetAlert> {
    @Mapping(target = "resolvedBy", source = "resolvedBy", qualifiedByName = "userLogin")
    TimesheetAlertDTO toDto(TimesheetAlert s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
