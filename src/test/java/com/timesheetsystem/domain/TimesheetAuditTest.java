package com.timesheetsystem.domain;

import static com.timesheetsystem.domain.TimesheetAuditTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimesheetAuditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimesheetAudit.class);
        TimesheetAudit timesheetAudit1 = getTimesheetAuditSample1();
        TimesheetAudit timesheetAudit2 = new TimesheetAudit();
        assertThat(timesheetAudit1).isNotEqualTo(timesheetAudit2);

        timesheetAudit2.setId(timesheetAudit1.getId());
        assertThat(timesheetAudit1).isEqualTo(timesheetAudit2);

        timesheetAudit2 = getTimesheetAuditSample2();
        assertThat(timesheetAudit1).isNotEqualTo(timesheetAudit2);
    }
}
