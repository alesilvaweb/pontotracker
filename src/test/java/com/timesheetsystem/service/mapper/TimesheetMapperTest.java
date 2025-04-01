package com.timesheetsystem.service.mapper;

import static com.timesheetsystem.domain.TimesheetAsserts.*;
import static com.timesheetsystem.domain.TimesheetTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimesheetMapperTest {

    private TimesheetMapper timesheetMapper;

    @BeforeEach
    void setUp() {
        timesheetMapper = new TimesheetMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTimesheetSample1();
        var actual = timesheetMapper.toEntity(timesheetMapper.toDto(expected));
        assertTimesheetAllPropertiesEquals(expected, actual);
    }
}
