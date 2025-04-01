package com.timesheetsystem.domain;

import static com.timesheetsystem.domain.TimesheetAlertTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimesheetAlertTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimesheetAlert.class);
        TimesheetAlert timesheetAlert1 = getTimesheetAlertSample1();
        TimesheetAlert timesheetAlert2 = new TimesheetAlert();
        assertThat(timesheetAlert1).isNotEqualTo(timesheetAlert2);

        timesheetAlert2.setId(timesheetAlert1.getId());
        assertThat(timesheetAlert1).isEqualTo(timesheetAlert2);

        timesheetAlert2 = getTimesheetAlertSample2();
        assertThat(timesheetAlert1).isNotEqualTo(timesheetAlert2);
    }
}
