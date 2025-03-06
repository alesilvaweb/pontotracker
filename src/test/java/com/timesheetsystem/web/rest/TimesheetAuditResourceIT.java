package com.timesheetsystem.web.rest;

import static com.timesheetsystem.domain.TimesheetAuditAsserts.*;
import static com.timesheetsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static com.timesheetsystem.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timesheetsystem.IntegrationTest;
import com.timesheetsystem.domain.TimesheetAudit;
import com.timesheetsystem.repository.TimesheetAuditRepository;
import com.timesheetsystem.service.dto.TimesheetAuditDTO;
import com.timesheetsystem.service.mapper.TimesheetAuditMapper;
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
 * Integration tests for the {@link TimesheetAuditResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimesheetAuditResourceIT {

    private static final String DEFAULT_ENTITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_OLD_VALUES = "AAAAAAAAAA";
    private static final String UPDATED_OLD_VALUES = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_VALUES = "AAAAAAAAAA";
    private static final String UPDATED_NEW_VALUES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/timesheet-audits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TimesheetAuditRepository timesheetAuditRepository;

    @Autowired
    private TimesheetAuditMapper timesheetAuditMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimesheetAuditMockMvc;

    private TimesheetAudit timesheetAudit;

    private TimesheetAudit insertedTimesheetAudit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimesheetAudit createEntity(EntityManager em) {
        TimesheetAudit timesheetAudit = new TimesheetAudit()
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityId(DEFAULT_ENTITY_ID)
            .action(DEFAULT_ACTION)
            .timestamp(DEFAULT_TIMESTAMP)
            .userId(DEFAULT_USER_ID)
            .oldValues(DEFAULT_OLD_VALUES)
            .newValues(DEFAULT_NEW_VALUES);
        return timesheetAudit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimesheetAudit createUpdatedEntity(EntityManager em) {
        TimesheetAudit timesheetAudit = new TimesheetAudit()
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .action(UPDATED_ACTION)
            .timestamp(UPDATED_TIMESTAMP)
            .userId(UPDATED_USER_ID)
            .oldValues(UPDATED_OLD_VALUES)
            .newValues(UPDATED_NEW_VALUES);
        return timesheetAudit;
    }

    @BeforeEach
    public void initTest() {
        timesheetAudit = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTimesheetAudit != null) {
            timesheetAuditRepository.delete(insertedTimesheetAudit);
            insertedTimesheetAudit = null;
        }
    }

    @Test
    @Transactional
    void createTimesheetAudit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TimesheetAudit
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);
        var returnedTimesheetAuditDTO = om.readValue(
            restTimesheetAuditMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAuditDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TimesheetAuditDTO.class
        );

        // Validate the TimesheetAudit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTimesheetAudit = timesheetAuditMapper.toEntity(returnedTimesheetAuditDTO);
        assertTimesheetAuditUpdatableFieldsEquals(returnedTimesheetAudit, getPersistedTimesheetAudit(returnedTimesheetAudit));

        insertedTimesheetAudit = returnedTimesheetAudit;
    }

    @Test
    @Transactional
    void createTimesheetAuditWithExistingId() throws Exception {
        // Create the TimesheetAudit with an existing ID
        timesheetAudit.setId(1L);
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimesheetAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAuditDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TimesheetAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEntityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAudit.setEntityType(null);

        // Create the TimesheetAudit, which fails.
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);

        restTimesheetAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAudit.setEntityId(null);

        // Create the TimesheetAudit, which fails.
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);

        restTimesheetAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAudit.setAction(null);

        // Create the TimesheetAudit, which fails.
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);

        restTimesheetAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAudit.setTimestamp(null);

        // Create the TimesheetAudit, which fails.
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);

        restTimesheetAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAudit.setUserId(null);

        // Create the TimesheetAudit, which fails.
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);

        restTimesheetAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTimesheetAudits() throws Exception {
        // Initialize the database
        insertedTimesheetAudit = timesheetAuditRepository.saveAndFlush(timesheetAudit);

        // Get all the timesheetAuditList
        restTimesheetAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timesheetAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].oldValues").value(hasItem(DEFAULT_OLD_VALUES)))
            .andExpect(jsonPath("$.[*].newValues").value(hasItem(DEFAULT_NEW_VALUES)));
    }

    @Test
    @Transactional
    void getTimesheetAudit() throws Exception {
        // Initialize the database
        insertedTimesheetAudit = timesheetAuditRepository.saveAndFlush(timesheetAudit);

        // Get the timesheetAudit
        restTimesheetAuditMockMvc
            .perform(get(ENTITY_API_URL_ID, timesheetAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timesheetAudit.getId().intValue()))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.oldValues").value(DEFAULT_OLD_VALUES))
            .andExpect(jsonPath("$.newValues").value(DEFAULT_NEW_VALUES));
    }

    @Test
    @Transactional
    void getNonExistingTimesheetAudit() throws Exception {
        // Get the timesheetAudit
        restTimesheetAuditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimesheetAudit() throws Exception {
        // Initialize the database
        insertedTimesheetAudit = timesheetAuditRepository.saveAndFlush(timesheetAudit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timesheetAudit
        TimesheetAudit updatedTimesheetAudit = timesheetAuditRepository.findById(timesheetAudit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTimesheetAudit are not directly saved in db
        em.detach(updatedTimesheetAudit);
        updatedTimesheetAudit
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .action(UPDATED_ACTION)
            .timestamp(UPDATED_TIMESTAMP)
            .userId(UPDATED_USER_ID)
            .oldValues(UPDATED_OLD_VALUES)
            .newValues(UPDATED_NEW_VALUES);
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(updatedTimesheetAudit);

        restTimesheetAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timesheetAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timesheetAuditDTO))
            )
            .andExpect(status().isOk());

        // Validate the TimesheetAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTimesheetAuditToMatchAllProperties(updatedTimesheetAudit);
    }

    @Test
    @Transactional
    void putNonExistingTimesheetAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetAudit.setId(longCount.incrementAndGet());

        // Create the TimesheetAudit
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimesheetAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timesheetAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timesheetAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimesheetAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimesheetAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetAudit.setId(longCount.incrementAndGet());

        // Create the TimesheetAudit
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timesheetAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimesheetAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimesheetAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetAudit.setId(longCount.incrementAndGet());

        // Create the TimesheetAudit
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetAuditMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAuditDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimesheetAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimesheetAuditWithPatch() throws Exception {
        // Initialize the database
        insertedTimesheetAudit = timesheetAuditRepository.saveAndFlush(timesheetAudit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timesheetAudit using partial update
        TimesheetAudit partialUpdatedTimesheetAudit = new TimesheetAudit();
        partialUpdatedTimesheetAudit.setId(timesheetAudit.getId());

        partialUpdatedTimesheetAudit.timestamp(UPDATED_TIMESTAMP).newValues(UPDATED_NEW_VALUES);

        restTimesheetAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimesheetAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimesheetAudit))
            )
            .andExpect(status().isOk());

        // Validate the TimesheetAudit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimesheetAuditUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTimesheetAudit, timesheetAudit),
            getPersistedTimesheetAudit(timesheetAudit)
        );
    }

    @Test
    @Transactional
    void fullUpdateTimesheetAuditWithPatch() throws Exception {
        // Initialize the database
        insertedTimesheetAudit = timesheetAuditRepository.saveAndFlush(timesheetAudit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timesheetAudit using partial update
        TimesheetAudit partialUpdatedTimesheetAudit = new TimesheetAudit();
        partialUpdatedTimesheetAudit.setId(timesheetAudit.getId());

        partialUpdatedTimesheetAudit
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .action(UPDATED_ACTION)
            .timestamp(UPDATED_TIMESTAMP)
            .userId(UPDATED_USER_ID)
            .oldValues(UPDATED_OLD_VALUES)
            .newValues(UPDATED_NEW_VALUES);

        restTimesheetAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimesheetAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimesheetAudit))
            )
            .andExpect(status().isOk());

        // Validate the TimesheetAudit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimesheetAuditUpdatableFieldsEquals(partialUpdatedTimesheetAudit, getPersistedTimesheetAudit(partialUpdatedTimesheetAudit));
    }

    @Test
    @Transactional
    void patchNonExistingTimesheetAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetAudit.setId(longCount.incrementAndGet());

        // Create the TimesheetAudit
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimesheetAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timesheetAuditDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timesheetAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimesheetAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimesheetAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetAudit.setId(longCount.incrementAndGet());

        // Create the TimesheetAudit
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timesheetAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimesheetAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimesheetAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetAudit.setId(longCount.incrementAndGet());

        // Create the TimesheetAudit
        TimesheetAuditDTO timesheetAuditDTO = timesheetAuditMapper.toDto(timesheetAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetAuditMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(timesheetAuditDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimesheetAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimesheetAudit() throws Exception {
        // Initialize the database
        insertedTimesheetAudit = timesheetAuditRepository.saveAndFlush(timesheetAudit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the timesheetAudit
        restTimesheetAuditMockMvc
            .perform(delete(ENTITY_API_URL_ID, timesheetAudit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return timesheetAuditRepository.count();
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

    protected TimesheetAudit getPersistedTimesheetAudit(TimesheetAudit timesheetAudit) {
        return timesheetAuditRepository.findById(timesheetAudit.getId()).orElseThrow();
    }

    protected void assertPersistedTimesheetAuditToMatchAllProperties(TimesheetAudit expectedTimesheetAudit) {
        assertTimesheetAuditAllPropertiesEquals(expectedTimesheetAudit, getPersistedTimesheetAudit(expectedTimesheetAudit));
    }

    protected void assertPersistedTimesheetAuditToMatchUpdatableProperties(TimesheetAudit expectedTimesheetAudit) {
        assertTimesheetAuditAllUpdatablePropertiesEquals(expectedTimesheetAudit, getPersistedTimesheetAudit(expectedTimesheetAudit));
    }
}
