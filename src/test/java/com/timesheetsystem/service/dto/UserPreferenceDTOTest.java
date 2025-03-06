package com.timesheetsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.timesheetsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserPreferenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPreferenceDTO.class);
        UserPreferenceDTO userPreferenceDTO1 = new UserPreferenceDTO();
        userPreferenceDTO1.setId(1L);
        UserPreferenceDTO userPreferenceDTO2 = new UserPreferenceDTO();
        assertThat(userPreferenceDTO1).isNotEqualTo(userPreferenceDTO2);
        userPreferenceDTO2.setId(userPreferenceDTO1.getId());
        assertThat(userPreferenceDTO1).isEqualTo(userPreferenceDTO2);
        userPreferenceDTO2.setId(2L);
        assertThat(userPreferenceDTO1).isNotEqualTo(userPreferenceDTO2);
        userPreferenceDTO1.setId(null);
        assertThat(userPreferenceDTO1).isNotEqualTo(userPreferenceDTO2);
    }
}
