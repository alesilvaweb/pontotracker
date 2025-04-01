package com.timesheetsystem.service.mapper;

import com.timesheetsystem.domain.TimeEntry;
import com.timesheetsystem.domain.Timesheet;
import com.timesheetsystem.service.dto.TimeEntryDTO;
import com.timesheetsystem.service.dto.TimesheetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimeEntry} and its DTO {@link TimeEntryDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimeEntryMapper extends EntityMapper<TimeEntryDTO, TimeEntry> {
    @Mapping(target = "timesheet", source = "timesheet", qualifiedByName = "timesheetId")
    TimeEntryDTO toDto(TimeEntry s);

    @Named("timesheetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TimesheetDTO toDtoTimesheetId(Timesheet timesheet);
}
