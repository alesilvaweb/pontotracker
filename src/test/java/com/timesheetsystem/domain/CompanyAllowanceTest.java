package com.timesheetsystem.domain;

import static com.timesheetsystem.domain.CompanyAllowanceTestSamples.*;
import static com.timesheetsystem.domain.CompanyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyAllowanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyAllowance.class);
        CompanyAllowance companyAllowance1 = getCompanyAllowanceSample1();
        CompanyAllowance companyAllowance2 = new CompanyAllowance();
        assertThat(companyAllowance1).isNotEqualTo(companyAllowance2);

        companyAllowance2.setId(companyAllowance1.getId());
        assertThat(companyAllowance1).isEqualTo(companyAllowance2);

        companyAllowance2 = getCompanyAllowanceSample2();
        assertThat(companyAllowance1).isNotEqualTo(companyAllowance2);
    }

    @Test
    void companyTest() {
        CompanyAllowance companyAllowance = getCompanyAllowanceRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        companyAllowance.setCompany(companyBack);
        assertThat(companyAllowance.getCompany()).isEqualTo(companyBack);

        companyAllowance.company(null);
        assertThat(companyAllowance.getCompany()).isNull();
    }
}
