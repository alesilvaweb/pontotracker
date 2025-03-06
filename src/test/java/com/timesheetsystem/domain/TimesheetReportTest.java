package com.timesheetsystem.domain;

import static com.timesheetsystem.domain.TimesheetReportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimesheetReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimesheetReport.class);
        TimesheetReport timesheetReport1 = getTimesheetReportSample1();
        TimesheetReport timesheetReport2 = new TimesheetReport();
        assertThat(timesheetReport1).isNotEqualTo(timesheetReport2);

        timesheetReport2.setId(timesheetReport1.getId());
        assertThat(timesheetReport1).isEqualTo(timesheetReport2);

        timesheetReport2 = getTimesheetReportSample2();
        assertThat(timesheetReport1).isNotEqualTo(timesheetReport2);
    }
}
