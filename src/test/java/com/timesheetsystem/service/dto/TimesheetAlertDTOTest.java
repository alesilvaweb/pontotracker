package com.timesheetsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimesheetAlertDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimesheetAlertDTO.class);
        TimesheetAlertDTO timesheetAlertDTO1 = new TimesheetAlertDTO();
        timesheetAlertDTO1.setId(1L);
        TimesheetAlertDTO timesheetAlertDTO2 = new TimesheetAlertDTO();
        assertThat(timesheetAlertDTO1).isNotEqualTo(timesheetAlertDTO2);
        timesheetAlertDTO2.setId(timesheetAlertDTO1.getId());
        assertThat(timesheetAlertDTO1).isEqualTo(timesheetAlertDTO2);
        timesheetAlertDTO2.setId(2L);
        assertThat(timesheetAlertDTO1).isNotEqualTo(timesheetAlertDTO2);
        timesheetAlertDTO1.setId(null);
        assertThat(timesheetAlertDTO1).isNotEqualTo(timesheetAlertDTO2);
    }
}
