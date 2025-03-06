package com.timesheetsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimesheetReportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimesheetReportDTO.class);
        TimesheetReportDTO timesheetReportDTO1 = new TimesheetReportDTO();
        timesheetReportDTO1.setId(1L);
        TimesheetReportDTO timesheetReportDTO2 = new TimesheetReportDTO();
        assertThat(timesheetReportDTO1).isNotEqualTo(timesheetReportDTO2);
        timesheetReportDTO2.setId(timesheetReportDTO1.getId());
        assertThat(timesheetReportDTO1).isEqualTo(timesheetReportDTO2);
        timesheetReportDTO2.setId(2L);
        assertThat(timesheetReportDTO1).isNotEqualTo(timesheetReportDTO2);
        timesheetReportDTO1.setId(null);
        assertThat(timesheetReportDTO1).isNotEqualTo(timesheetReportDTO2);
    }
}
