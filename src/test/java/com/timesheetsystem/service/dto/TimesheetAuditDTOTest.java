package com.timesheetsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimesheetAuditDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimesheetAuditDTO.class);
        TimesheetAuditDTO timesheetAuditDTO1 = new TimesheetAuditDTO();
        timesheetAuditDTO1.setId(1L);
        TimesheetAuditDTO timesheetAuditDTO2 = new TimesheetAuditDTO();
        assertThat(timesheetAuditDTO1).isNotEqualTo(timesheetAuditDTO2);
        timesheetAuditDTO2.setId(timesheetAuditDTO1.getId());
        assertThat(timesheetAuditDTO1).isEqualTo(timesheetAuditDTO2);
        timesheetAuditDTO2.setId(2L);
        assertThat(timesheetAuditDTO1).isNotEqualTo(timesheetAuditDTO2);
        timesheetAuditDTO1.setId(null);
        assertThat(timesheetAuditDTO1).isNotEqualTo(timesheetAuditDTO2);
    }
}
