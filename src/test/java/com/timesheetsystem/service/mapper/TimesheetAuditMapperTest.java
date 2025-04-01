package com.timesheetsystem.service.mapper;

import static com.timesheetsystem.domain.TimesheetAuditAsserts.*;
import static com.timesheetsystem.domain.TimesheetAuditTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimesheetAuditMapperTest {

    private TimesheetAuditMapper timesheetAuditMapper;

    @BeforeEach
    void setUp() {
        timesheetAuditMapper = new TimesheetAuditMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTimesheetAuditSample1();
        var actual = timesheetAuditMapper.toEntity(timesheetAuditMapper.toDto(expected));
        assertTimesheetAuditAllPropertiesEquals(expected, actual);
    }
}
