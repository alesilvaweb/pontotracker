package com.timesheetsystem.service.mapper;

import static com.timesheetsystem.domain.TimesheetAlertAsserts.*;
import static com.timesheetsystem.domain.TimesheetAlertTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimesheetAlertMapperTest {

    private TimesheetAlertMapper timesheetAlertMapper;

    @BeforeEach
    void setUp() {
        timesheetAlertMapper = new TimesheetAlertMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTimesheetAlertSample1();
        var actual = timesheetAlertMapper.toEntity(timesheetAlertMapper.toDto(expected));
        assertTimesheetAlertAllPropertiesEquals(expected, actual);
    }
}
