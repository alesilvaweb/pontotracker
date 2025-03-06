package com.timesheetsystem.domain;

import static com.timesheetsystem.domain.TimeEntryTestSamples.*;
import static com.timesheetsystem.domain.TimesheetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimeEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeEntry.class);
        TimeEntry timeEntry1 = getTimeEntrySample1();
        TimeEntry timeEntry2 = new TimeEntry();
        assertThat(timeEntry1).isNotEqualTo(timeEntry2);

        timeEntry2.setId(timeEntry1.getId());
        assertThat(timeEntry1).isEqualTo(timeEntry2);

        timeEntry2 = getTimeEntrySample2();
        assertThat(timeEntry1).isNotEqualTo(timeEntry2);
    }

    @Test
    void timesheetTest() {
        TimeEntry timeEntry = getTimeEntryRandomSampleGenerator();
        Timesheet timesheetBack = getTimesheetRandomSampleGenerator();

        timeEntry.setTimesheet(timesheetBack);
        assertThat(timeEntry.getTimesheet()).isEqualTo(timesheetBack);

        timeEntry.timesheet(null);
        assertThat(timeEntry.getTimesheet()).isNull();
    }
}
