package com.timesheetsystem.service.mapper;

import static com.timesheetsystem.domain.SystemConfigAsserts.*;
import static com.timesheetsystem.domain.SystemConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemConfigMapperTest {

    private SystemConfigMapper systemConfigMapper;

    @BeforeEach
    void setUp() {
        systemConfigMapper = new SystemConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSystemConfigSample1();
        var actual = systemConfigMapper.toEntity(systemConfigMapper.toDto(expected));
        assertSystemConfigAllPropertiesEquals(expected, actual);
    }
}
