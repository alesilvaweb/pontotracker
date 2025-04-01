package com.timesheetsystem.domain;

import static com.timesheetsystem.domain.CompanyAllowanceTestSamples.*;
import static com.timesheetsystem.domain.CompanyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = getCompanySample1();
        Company company2 = new Company();
        assertThat(company1).isNotEqualTo(company2);

        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);

        company2 = getCompanySample2();
        assertThat(company1).isNotEqualTo(company2);
    }

    @Test
    void companyAllowanceTest() {
        Company company = getCompanyRandomSampleGenerator();
        CompanyAllowance companyAllowanceBack = getCompanyAllowanceRandomSampleGenerator();

        company.setCompanyAllowance(companyAllowanceBack);
        assertThat(company.getCompanyAllowance()).isEqualTo(companyAllowanceBack);
        assertThat(companyAllowanceBack.getCompany()).isEqualTo(company);

        company.companyAllowance(null);
        assertThat(company.getCompanyAllowance()).isNull();
        assertThat(companyAllowanceBack.getCompany()).isNull();
    }
}
