package com.timesheetsystem.service.mapper;

import com.timesheetsystem.domain.TimesheetReport;
import com.timesheetsystem.domain.User;
import com.timesheetsystem.service.dto.TimesheetReportDTO;
import com.timesheetsystem.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimesheetReport} and its DTO {@link TimesheetReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimesheetReportMapper extends EntityMapper<TimesheetReportDTO, TimesheetReport> {
    @Mapping(target = "generatedBy", source = "generatedBy", qualifiedByName = "userLogin")
    @Mapping(target = "approvedBy", source = "approvedBy", qualifiedByName = "userLogin")
    TimesheetReportDTO toDto(TimesheetReport s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
