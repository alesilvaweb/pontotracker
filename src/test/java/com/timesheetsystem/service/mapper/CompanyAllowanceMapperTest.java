package com.timesheetsystem.service.mapper;

import static com.timesheetsystem.domain.CompanyAllowanceAsserts.*;
import static com.timesheetsystem.domain.CompanyAllowanceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompanyAllowanceMapperTest {

    private CompanyAllowanceMapper companyAllowanceMapper;

    @BeforeEach
    void setUp() {
        companyAllowanceMapper = new CompanyAllowanceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCompanyAllowanceSample1();
        var actual = companyAllowanceMapper.toEntity(companyAllowanceMapper.toDto(expected));
        assertCompanyAllowanceAllPropertiesEquals(expected, actual);
    }
}
