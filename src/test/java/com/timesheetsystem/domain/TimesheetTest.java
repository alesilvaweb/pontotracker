package com.timesheetsystem.domain;

import static com.timesheetsystem.domain.CompanyTestSamples.*;
import static com.timesheetsystem.domain.TimeEntryTestSamples.*;
import static com.timesheetsystem.domain.TimesheetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TimesheetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Timesheet.class);
        Timesheet timesheet1 = getTimesheetSample1();
        Timesheet timesheet2 = new Timesheet();
        assertThat(timesheet1).isNotEqualTo(timesheet2);

        timesheet2.setId(timesheet1.getId());
        assertThat(timesheet1).isEqualTo(timesheet2);

        timesheet2 = getTimesheetSample2();
        assertThat(timesheet1).isNotEqualTo(timesheet2);
    }

    @Test
    void timeEntryTest() {
        Timesheet timesheet = getTimesheetRandomSampleGenerator();
        TimeEntry timeEntryBack = getTimeEntryRandomSampleGenerator();

        timesheet.addTimeEntry(timeEntryBack);
        assertThat(timesheet.getTimeEntries()).containsOnly(timeEntryBack);
        assertThat(timeEntryBack.getTimesheet()).isEqualTo(timesheet);

        timesheet.removeTimeEntry(timeEntryBack);
        assertThat(timesheet.getTimeEntries()).doesNotContain(timeEntryBack);
        assertThat(timeEntryBack.getTimesheet()).isNull();

        timesheet.timeEntries(new HashSet<>(Set.of(timeEntryBack)));
        assertThat(timesheet.getTimeEntries()).containsOnly(timeEntryBack);
        assertThat(timeEntryBack.getTimesheet()).isEqualTo(timesheet);

        timesheet.setTimeEntries(new HashSet<>());
        assertThat(timesheet.getTimeEntries()).doesNotContain(timeEntryBack);
        assertThat(timeEntryBack.getTimesheet()).isNull();
    }

    @Test
    void companyTest() {
        Timesheet timesheet = getTimesheetRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        timesheet.setCompany(companyBack);
        assertThat(timesheet.getCompany()).isEqualTo(companyBack);

        timesheet.company(null);
        assertThat(timesheet.getCompany()).isNull();
    }
}
