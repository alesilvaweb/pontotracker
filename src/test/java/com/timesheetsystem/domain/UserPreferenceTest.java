package com.timesheetsystem.domain;

import static com.timesheetsystem.domain.UserPreferenceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserPreferenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPreference.class);
        UserPreference userPreference1 = getUserPreferenceSample1();
        UserPreference userPreference2 = new UserPreference();
        assertThat(userPreference1).isNotEqualTo(userPreference2);

        userPreference2.setId(userPreference1.getId());
        assertThat(userPreference1).isEqualTo(userPreference2);

        userPreference2 = getUserPreferenceSample2();
        assertThat(userPreference1).isNotEqualTo(userPreference2);
    }
}
