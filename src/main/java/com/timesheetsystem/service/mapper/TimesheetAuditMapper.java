package com.timesheetsystem.service.mapper;

import com.timesheetsystem.domain.TimesheetAudit;
import com.timesheetsystem.service.dto.TimesheetAuditDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimesheetAudit} and its DTO {@link TimesheetAuditDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimesheetAuditMapper extends EntityMapper<TimesheetAuditDTO, TimesheetAudit> {}
