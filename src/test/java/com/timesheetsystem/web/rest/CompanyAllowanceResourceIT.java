package com.timesheetsystem.web.rest;

import static com.timesheetsystem.domain.CompanyAllowanceAsserts.*;
import static com.timesheetsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static com.timesheetsystem.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timesheetsystem.IntegrationTest;
import com.timesheetsystem.domain.Company;
import com.timesheetsystem.domain.CompanyAllowance;
import com.timesheetsystem.repository.CompanyAllowanceRepository;
import com.timesheetsystem.service.dto.CompanyAllowanceDTO;
import com.timesheetsystem.service.mapper.CompanyAllowanceMapper;
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
 * Integration tests for the {@link CompanyAllowanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyAllowanceResourceIT {

    private static final Double DEFAULT_PRESENTIAL_ALLOWANCE_VALUE = 1D;
    private static final Double UPDATED_PRESENTIAL_ALLOWANCE_VALUE = 2D;

    private static final Double DEFAULT_REMOTE_ALLOWANCE_VALUE = 0D;
    private static final Double UPDATED_REMOTE_ALLOWANCE_VALUE = 1D;

    private static final Double DEFAULT_FULL_TIME_THRESHOLD_HOURS = 0D;
    private static final Double UPDATED_FULL_TIME_THRESHOLD_HOURS = 1D;

    private static final Integer DEFAULT_PART_TIME_ALLOWANCE_PERCENTAGE = 0;
    private static final Integer UPDATED_PART_TIME_ALLOWANCE_PERCENTAGE = 1;

    private static final Boolean DEFAULT_CONSIDER_WORK_DISTANCE = false;
    private static final Boolean UPDATED_CONSIDER_WORK_DISTANCE = true;

    private static final Integer DEFAULT_MINIMUM_DISTANCE_TO_RECEIVE = 0;
    private static final Integer UPDATED_MINIMUM_DISTANCE_TO_RECEIVE = 1;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/company-allowances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompanyAllowanceRepository companyAllowanceRepository;

    @Autowired
    private CompanyAllowanceMapper companyAllowanceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyAllowanceMockMvc;

    private CompanyAllowance companyAllowance;

    private CompanyAllowance insertedCompanyAllowance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyAllowance createEntity(EntityManager em) {
        CompanyAllowance companyAllowance = new CompanyAllowance()
            .presentialAllowanceValue(DEFAULT_PRESENTIAL_ALLOWANCE_VALUE)
            .remoteAllowanceValue(DEFAULT_REMOTE_ALLOWANCE_VALUE)
            .fullTimeThresholdHours(DEFAULT_FULL_TIME_THRESHOLD_HOURS)
            .partTimeAllowancePercentage(DEFAULT_PART_TIME_ALLOWANCE_PERCENTAGE)
            .considerWorkDistance(DEFAULT_CONSIDER_WORK_DISTANCE)
            .minimumDistanceToReceive(DEFAULT_MINIMUM_DISTANCE_TO_RECEIVE)
            .active(DEFAULT_ACTIVE)
            .lastUpdated(DEFAULT_LAST_UPDATED);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        companyAllowance.setCompany(company);
        return companyAllowance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyAllowance createUpdatedEntity(EntityManager em) {
        CompanyAllowance companyAllowance = new CompanyAllowance()
            .presentialAllowanceValue(UPDATED_PRESENTIAL_ALLOWANCE_VALUE)
            .remoteAllowanceValue(UPDATED_REMOTE_ALLOWANCE_VALUE)
            .fullTimeThresholdHours(UPDATED_FULL_TIME_THRESHOLD_HOURS)
            .partTimeAllowancePercentage(UPDATED_PART_TIME_ALLOWANCE_PERCENTAGE)
            .considerWorkDistance(UPDATED_CONSIDER_WORK_DISTANCE)
            .minimumDistanceToReceive(UPDATED_MINIMUM_DISTANCE_TO_RECEIVE)
            .active(UPDATED_ACTIVE)
            .lastUpdated(UPDATED_LAST_UPDATED);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        companyAllowance.setCompany(company);
        return companyAllowance;
    }

    @BeforeEach
    public void initTest() {
        companyAllowance = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCompanyAllowance != null) {
            companyAllowanceRepository.delete(insertedCompanyAllowance);
            insertedCompanyAllowance = null;
        }
    }

    @Test
    @Transactional
    void createCompanyAllowance() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CompanyAllowance
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);
        var returnedCompanyAllowanceDTO = om.readValue(
            restCompanyAllowanceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyAllowanceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompanyAllowanceDTO.class
        );

        // Validate the CompanyAllowance in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompanyAllowance = companyAllowanceMapper.toEntity(returnedCompanyAllowanceDTO);
        assertCompanyAllowanceUpdatableFieldsEquals(returnedCompanyAllowance, getPersistedCompanyAllowance(returnedCompanyAllowance));

        insertedCompanyAllowance = returnedCompanyAllowance;
    }

    @Test
    @Transactional
    void createCompanyAllowanceWithExistingId() throws Exception {
        // Create the CompanyAllowance with an existing ID
        companyAllowance.setId(1L);
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyAllowanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyAllowanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CompanyAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPresentialAllowanceValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyAllowance.setPresentialAllowanceValue(null);

        // Create the CompanyAllowance, which fails.
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        restCompanyAllowanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyAllowanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRemoteAllowanceValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyAllowance.setRemoteAllowanceValue(null);

        // Create the CompanyAllowance, which fails.
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        restCompanyAllowanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyAllowanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFullTimeThresholdHoursIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyAllowance.setFullTimeThresholdHours(null);

        // Create the CompanyAllowance, which fails.
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        restCompanyAllowanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyAllowanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPartTimeAllowancePercentageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyAllowance.setPartTimeAllowancePercentage(null);

        // Create the CompanyAllowance, which fails.
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        restCompanyAllowanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyAllowanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConsiderWorkDistanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyAllowance.setConsiderWorkDistance(null);

        // Create the CompanyAllowance, which fails.
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        restCompanyAllowanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyAllowanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyAllowance.setActive(null);

        // Create the CompanyAllowance, which fails.
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        restCompanyAllowanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyAllowanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        companyAllowance.setLastUpdated(null);

        // Create the CompanyAllowance, which fails.
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        restCompanyAllowanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyAllowanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompanyAllowances() throws Exception {
        // Initialize the database
        insertedCompanyAllowance = companyAllowanceRepository.saveAndFlush(companyAllowance);

        // Get all the companyAllowanceList
        restCompanyAllowanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyAllowance.getId().intValue())))
            .andExpect(jsonPath("$.[*].presentialAllowanceValue").value(hasItem(DEFAULT_PRESENTIAL_ALLOWANCE_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].remoteAllowanceValue").value(hasItem(DEFAULT_REMOTE_ALLOWANCE_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].fullTimeThresholdHours").value(hasItem(DEFAULT_FULL_TIME_THRESHOLD_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].partTimeAllowancePercentage").value(hasItem(DEFAULT_PART_TIME_ALLOWANCE_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].considerWorkDistance").value(hasItem(DEFAULT_CONSIDER_WORK_DISTANCE.booleanValue())))
            .andExpect(jsonPath("$.[*].minimumDistanceToReceive").value(hasItem(DEFAULT_MINIMUM_DISTANCE_TO_RECEIVE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED))));
    }

    @Test
    @Transactional
    void getCompanyAllowance() throws Exception {
        // Initialize the database
        insertedCompanyAllowance = companyAllowanceRepository.saveAndFlush(companyAllowance);

        // Get the companyAllowance
        restCompanyAllowanceMockMvc
            .perform(get(ENTITY_API_URL_ID, companyAllowance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companyAllowance.getId().intValue()))
            .andExpect(jsonPath("$.presentialAllowanceValue").value(DEFAULT_PRESENTIAL_ALLOWANCE_VALUE.doubleValue()))
            .andExpect(jsonPath("$.remoteAllowanceValue").value(DEFAULT_REMOTE_ALLOWANCE_VALUE.doubleValue()))
            .andExpect(jsonPath("$.fullTimeThresholdHours").value(DEFAULT_FULL_TIME_THRESHOLD_HOURS.doubleValue()))
            .andExpect(jsonPath("$.partTimeAllowancePercentage").value(DEFAULT_PART_TIME_ALLOWANCE_PERCENTAGE))
            .andExpect(jsonPath("$.considerWorkDistance").value(DEFAULT_CONSIDER_WORK_DISTANCE.booleanValue()))
            .andExpect(jsonPath("$.minimumDistanceToReceive").value(DEFAULT_MINIMUM_DISTANCE_TO_RECEIVE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.lastUpdated").value(sameInstant(DEFAULT_LAST_UPDATED)));
    }

    @Test
    @Transactional
    void getNonExistingCompanyAllowance() throws Exception {
        // Get the companyAllowance
        restCompanyAllowanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompanyAllowance() throws Exception {
        // Initialize the database
        insertedCompanyAllowance = companyAllowanceRepository.saveAndFlush(companyAllowance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the companyAllowance
        CompanyAllowance updatedCompanyAllowance = companyAllowanceRepository.findById(companyAllowance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompanyAllowance are not directly saved in db
        em.detach(updatedCompanyAllowance);
        updatedCompanyAllowance
            .presentialAllowanceValue(UPDATED_PRESENTIAL_ALLOWANCE_VALUE)
            .remoteAllowanceValue(UPDATED_REMOTE_ALLOWANCE_VALUE)
            .fullTimeThresholdHours(UPDATED_FULL_TIME_THRESHOLD_HOURS)
            .partTimeAllowancePercentage(UPDATED_PART_TIME_ALLOWANCE_PERCENTAGE)
            .considerWorkDistance(UPDATED_CONSIDER_WORK_DISTANCE)
            .minimumDistanceToReceive(UPDATED_MINIMUM_DISTANCE_TO_RECEIVE)
            .active(UPDATED_ACTIVE)
            .lastUpdated(UPDATED_LAST_UPDATED);
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(updatedCompanyAllowance);

        restCompanyAllowanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyAllowanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(companyAllowanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompanyAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompanyAllowanceToMatchAllProperties(updatedCompanyAllowance);
    }

    @Test
    @Transactional
    void putNonExistingCompanyAllowance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyAllowance.setId(longCount.incrementAndGet());

        // Create the CompanyAllowance
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyAllowanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companyAllowanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(companyAllowanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanyAllowance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyAllowance.setId(longCount.incrementAndGet());

        // Create the CompanyAllowance
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyAllowanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(companyAllowanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanyAllowance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyAllowance.setId(longCount.incrementAndGet());

        // Create the CompanyAllowance
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyAllowanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(companyAllowanceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyAllowanceWithPatch() throws Exception {
        // Initialize the database
        insertedCompanyAllowance = companyAllowanceRepository.saveAndFlush(companyAllowance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the companyAllowance using partial update
        CompanyAllowance partialUpdatedCompanyAllowance = new CompanyAllowance();
        partialUpdatedCompanyAllowance.setId(companyAllowance.getId());

        partialUpdatedCompanyAllowance
            .presentialAllowanceValue(UPDATED_PRESENTIAL_ALLOWANCE_VALUE)
            .considerWorkDistance(UPDATED_CONSIDER_WORK_DISTANCE)
            .minimumDistanceToReceive(UPDATED_MINIMUM_DISTANCE_TO_RECEIVE);

        restCompanyAllowanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyAllowance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompanyAllowance))
            )
            .andExpect(status().isOk());

        // Validate the CompanyAllowance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyAllowanceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCompanyAllowance, companyAllowance),
            getPersistedCompanyAllowance(companyAllowance)
        );
    }

    @Test
    @Transactional
    void fullUpdateCompanyAllowanceWithPatch() throws Exception {
        // Initialize the database
        insertedCompanyAllowance = companyAllowanceRepository.saveAndFlush(companyAllowance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the companyAllowance using partial update
        CompanyAllowance partialUpdatedCompanyAllowance = new CompanyAllowance();
        partialUpdatedCompanyAllowance.setId(companyAllowance.getId());

        partialUpdatedCompanyAllowance
            .presentialAllowanceValue(UPDATED_PRESENTIAL_ALLOWANCE_VALUE)
            .remoteAllowanceValue(UPDATED_REMOTE_ALLOWANCE_VALUE)
            .fullTimeThresholdHours(UPDATED_FULL_TIME_THRESHOLD_HOURS)
            .partTimeAllowancePercentage(UPDATED_PART_TIME_ALLOWANCE_PERCENTAGE)
            .considerWorkDistance(UPDATED_CONSIDER_WORK_DISTANCE)
            .minimumDistanceToReceive(UPDATED_MINIMUM_DISTANCE_TO_RECEIVE)
            .active(UPDATED_ACTIVE)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restCompanyAllowanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanyAllowance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompanyAllowance))
            )
            .andExpect(status().isOk());

        // Validate the CompanyAllowance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyAllowanceUpdatableFieldsEquals(
            partialUpdatedCompanyAllowance,
            getPersistedCompanyAllowance(partialUpdatedCompanyAllowance)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCompanyAllowance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyAllowance.setId(longCount.incrementAndGet());

        // Create the CompanyAllowance
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyAllowanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companyAllowanceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(companyAllowanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanyAllowance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyAllowance.setId(longCount.incrementAndGet());

        // Create the CompanyAllowance
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyAllowanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(companyAllowanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompanyAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanyAllowance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        companyAllowance.setId(longCount.incrementAndGet());

        // Create the CompanyAllowance
        CompanyAllowanceDTO companyAllowanceDTO = companyAllowanceMapper.toDto(companyAllowance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyAllowanceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(companyAllowanceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompanyAllowance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanyAllowance() throws Exception {
        // Initialize the database
        insertedCompanyAllowance = companyAllowanceRepository.saveAndFlush(companyAllowance);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the companyAllowance
        restCompanyAllowanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, companyAllowance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return companyAllowanceRepository.count();
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

    protected CompanyAllowance getPersistedCompanyAllowance(CompanyAllowance companyAllowance) {
        return companyAllowanceRepository.findById(companyAllowance.getId()).orElseThrow();
    }

    protected void assertPersistedCompanyAllowanceToMatchAllProperties(CompanyAllowance expectedCompanyAllowance) {
        assertCompanyAllowanceAllPropertiesEquals(expectedCompanyAllowance, getPersistedCompanyAllowance(expectedCompanyAllowance));
    }

    protected void assertPersistedCompanyAllowanceToMatchUpdatableProperties(CompanyAllowance expectedCompanyAllowance) {
        assertCompanyAllowanceAllUpdatablePropertiesEquals(
            expectedCompanyAllowance,
            getPersistedCompanyAllowance(expectedCompanyAllowance)
        );
    }
}
