package com.timesheetsystem.service.mapper;

import static com.timesheetsystem.domain.TimesheetReportAsserts.*;
import static com.timesheetsystem.domain.TimesheetReportTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimesheetReportMapperTest {

    private TimesheetReportMapper timesheetReportMapper;

    @BeforeEach
    void setUp() {
        timesheetReportMapper = new TimesheetReportMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTimesheetReportSample1();
        var actual = timesheetReportMapper.toEntity(timesheetReportMapper.toDto(expected));
        assertTimesheetReportAllPropertiesEquals(expected, actual);
    }
}
