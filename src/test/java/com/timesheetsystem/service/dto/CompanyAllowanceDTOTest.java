package com.timesheetsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyAllowanceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyAllowanceDTO.class);
        CompanyAllowanceDTO companyAllowanceDTO1 = new CompanyAllowanceDTO();
        companyAllowanceDTO1.setId(1L);
        CompanyAllowanceDTO companyAllowanceDTO2 = new CompanyAllowanceDTO();
        assertThat(companyAllowanceDTO1).isNotEqualTo(companyAllowanceDTO2);
        companyAllowanceDTO2.setId(companyAllowanceDTO1.getId());
        assertThat(companyAllowanceDTO1).isEqualTo(companyAllowanceDTO2);
        companyAllowanceDTO2.setId(2L);
        assertThat(companyAllowanceDTO1).isNotEqualTo(companyAllowanceDTO2);
        companyAllowanceDTO1.setId(null);
        assertThat(companyAllowanceDTO1).isNotEqualTo(companyAllowanceDTO2);
    }
}
