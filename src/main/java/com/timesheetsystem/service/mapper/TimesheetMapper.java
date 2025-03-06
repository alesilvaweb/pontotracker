package com.timesheetsystem.service.mapper;

import com.timesheetsystem.domain.Company;
import com.timesheetsystem.domain.Timesheet;
import com.timesheetsystem.domain.User;
import com.timesheetsystem.service.dto.CompanyDTO;
import com.timesheetsystem.service.dto.TimesheetDTO;
import com.timesheetsystem.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Timesheet} and its DTO {@link TimesheetDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimesheetMapper extends EntityMapper<TimesheetDTO, Timesheet> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    TimesheetDTO toDto(Timesheet s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);
}
