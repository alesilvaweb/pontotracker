package com.timesheetsystem.service.mapper;

import static com.timesheetsystem.domain.UserPreferenceAsserts.*;
import static com.timesheetsystem.domain.UserPreferenceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserPreferenceMapperTest {

    private UserPreferenceMapper userPreferenceMapper;

    @BeforeEach
    void setUp() {
        userPreferenceMapper = new UserPreferenceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserPreferenceSample1();
        var actual = userPreferenceMapper.toEntity(userPreferenceMapper.toDto(expected));
        assertUserPreferenceAllPropertiesEquals(expected, actual);
    }
}
