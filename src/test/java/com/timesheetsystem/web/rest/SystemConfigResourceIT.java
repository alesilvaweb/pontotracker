package com.timesheetsystem.web.rest;

import static com.timesheetsystem.domain.SystemConfigAsserts.*;
import static com.timesheetsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static com.timesheetsystem.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timesheetsystem.IntegrationTest;
import com.timesheetsystem.domain.SystemConfig;
import com.timesheetsystem.repository.SystemConfigRepository;
import com.timesheetsystem.service.dto.SystemConfigDTO;
import com.timesheetsystem.service.mapper.SystemConfigMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SystemConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemConfigResourceIT {

    private static final Double DEFAULT_DAILY_WORK_HOURS = 1D;
    private static final Double UPDATED_DAILY_WORK_HOURS = 2D;

    private static final Double DEFAULT_WEEKLY_WORK_HOURS = 1D;
    private static final Double UPDATED_WEEKLY_WORK_HOURS = 2D;

    private static final Double DEFAULT_OVERTIME_NORMAL_RATE = 1D;
    private static final Double UPDATED_OVERTIME_NORMAL_RATE = 2D;

    private static final Double DEFAULT_OVERTIME_SPECIAL_RATE = 1D;
    private static final Double UPDATED_OVERTIME_SPECIAL_RATE = 2D;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/system-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemConfigMockMvc;

    private SystemConfig systemConfig;

    private SystemConfig insertedSystemConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemConfig createEntity(EntityManager em) {
        SystemConfig systemConfig = new SystemConfig()
            .dailyWorkHours(DEFAULT_DAILY_WORK_HOURS)
            .weeklyWorkHours(DEFAULT_WEEKLY_WORK_HOURS)
            .overtimeNormalRate(DEFAULT_OVERTIME_NORMAL_RATE)
            .overtimeSpecialRate(DEFAULT_OVERTIME_SPECIAL_RATE)
            .lastUpdated(DEFAULT_LAST_UPDATED);
        return systemConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemConfig createUpdatedEntity(EntityManager em) {
        SystemConfig systemConfig = new SystemConfig()
            .dailyWorkHours(UPDATED_DAILY_WORK_HOURS)
            .weeklyWorkHours(UPDATED_WEEKLY_WORK_HOURS)
            .overtimeNormalRate(UPDATED_OVERTIME_NORMAL_RATE)
            .overtimeSpecialRate(UPDATED_OVERTIME_SPECIAL_RATE)
            .lastUpdated(UPDATED_LAST_UPDATED);
        return systemConfig;
    }

    @BeforeEach
    public void initTest() {
        systemConfig = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSystemConfig != null) {
            systemConfigRepository.delete(insertedSystemConfig);
            insertedSystemConfig = null;
        }
    }

    @Test
    @Transactional
    void createSystemConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);
        var returnedSystemConfigDTO = om.readValue(
            restSystemConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SystemConfigDTO.class
        );

        // Validate the SystemConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSystemConfig = systemConfigMapper.toEntity(returnedSystemConfigDTO);
        assertSystemConfigUpdatableFieldsEquals(returnedSystemConfig, getPersistedSystemConfig(returnedSystemConfig));

        insertedSystemConfig = returnedSystemConfig;
    }

    @Test
    @Transactional
    void createSystemConfigWithExistingId() throws Exception {
        // Create the SystemConfig with an existing ID
        systemConfig.setId(1L);
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDailyWorkHoursIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfig.setDailyWorkHours(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeeklyWorkHoursIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfig.setWeeklyWorkHours(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOvertimeNormalRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfig.setOvertimeNormalRate(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOvertimeSpecialRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfig.setOvertimeSpecialRate(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemConfig.setLastUpdated(null);

        // Create the SystemConfig, which fails.
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        restSystemConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemConfigs() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndFlush(systemConfig);

        // Get all the systemConfigList
        restSystemConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].dailyWorkHours").value(hasItem(DEFAULT_DAILY_WORK_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].weeklyWorkHours").value(hasItem(DEFAULT_WEEKLY_WORK_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].overtimeNormalRate").value(hasItem(DEFAULT_OVERTIME_NORMAL_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].overtimeSpecialRate").value(hasItem(DEFAULT_OVERTIME_SPECIAL_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED))));
    }

    @Test
    @Transactional
    void getSystemConfig() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndFlush(systemConfig);

        // Get the systemConfig
        restSystemConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, systemConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemConfig.getId().intValue()))
            .andExpect(jsonPath("$.dailyWorkHours").value(DEFAULT_DAILY_WORK_HOURS.doubleValue()))
            .andExpect(jsonPath("$.weeklyWorkHours").value(DEFAULT_WEEKLY_WORK_HOURS.doubleValue()))
            .andExpect(jsonPath("$.overtimeNormalRate").value(DEFAULT_OVERTIME_NORMAL_RATE.doubleValue()))
            .andExpect(jsonPath("$.overtimeSpecialRate").value(DEFAULT_OVERTIME_SPECIAL_RATE.doubleValue()))
            .andExpect(jsonPath("$.lastUpdated").value(sameInstant(DEFAULT_LAST_UPDATED)));
    }

    @Test
    @Transactional
    void getNonExistingSystemConfig() throws Exception {
        // Get the systemConfig
        restSystemConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemConfig() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndFlush(systemConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemConfig
        SystemConfig updatedSystemConfig = systemConfigRepository.findById(systemConfig.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystemConfig are not directly saved in db
        em.detach(updatedSystemConfig);
        updatedSystemConfig
            .dailyWorkHours(UPDATED_DAILY_WORK_HOURS)
            .weeklyWorkHours(UPDATED_WEEKLY_WORK_HOURS)
            .overtimeNormalRate(UPDATED_OVERTIME_NORMAL_RATE)
            .overtimeSpecialRate(UPDATED_OVERTIME_SPECIAL_RATE)
            .lastUpdated(UPDATED_LAST_UPDATED);
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(updatedSystemConfig);

        restSystemConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSystemConfigToMatchAllProperties(updatedSystemConfig);
    }

    @Test
    @Transactional
    void putNonExistingSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemConfigWithPatch() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndFlush(systemConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemConfig using partial update
        SystemConfig partialUpdatedSystemConfig = new SystemConfig();
        partialUpdatedSystemConfig.setId(systemConfig.getId());

        partialUpdatedSystemConfig
            .dailyWorkHours(UPDATED_DAILY_WORK_HOURS)
            .overtimeNormalRate(UPDATED_OVERTIME_NORMAL_RATE)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemConfig))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSystemConfig, systemConfig),
            getPersistedSystemConfig(systemConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateSystemConfigWithPatch() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndFlush(systemConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemConfig using partial update
        SystemConfig partialUpdatedSystemConfig = new SystemConfig();
        partialUpdatedSystemConfig.setId(systemConfig.getId());

        partialUpdatedSystemConfig
            .dailyWorkHours(UPDATED_DAILY_WORK_HOURS)
            .weeklyWorkHours(UPDATED_WEEKLY_WORK_HOURS)
            .overtimeNormalRate(UPDATED_OVERTIME_NORMAL_RATE)
            .overtimeSpecialRate(UPDATED_OVERTIME_SPECIAL_RATE)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemConfig))
            )
            .andExpect(status().isOk());

        // Validate the SystemConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemConfigUpdatableFieldsEquals(partialUpdatedSystemConfig, getPersistedSystemConfig(partialUpdatedSystemConfig));
    }

    @Test
    @Transactional
    void patchNonExistingSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemConfig.setId(longCount.incrementAndGet());

        // Create the SystemConfig
        SystemConfigDTO systemConfigDTO = systemConfigMapper.toDto(systemConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(systemConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemConfig() throws Exception {
        // Initialize the database
        insertedSystemConfig = systemConfigRepository.saveAndFlush(systemConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the systemConfig
        restSystemConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return systemConfigRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected SystemConfig getPersistedSystemConfig(SystemConfig systemConfig) {
        return systemConfigRepository.findById(systemConfig.getId()).orElseThrow();
    }

    protected void assertPersistedSystemConfigToMatchAllProperties(SystemConfig expectedSystemConfig) {
        assertSystemConfigAllPropertiesEquals(expectedSystemConfig, getPersistedSystemConfig(expectedSystemConfig));
    }

    protected void assertPersistedSystemConfigToMatchUpdatableProperties(SystemConfig expectedSystemConfig) {
        assertSystemConfigAllUpdatablePropertiesEquals(expectedSystemConfig, getPersistedSystemConfig(expectedSystemConfig));
    }
}
