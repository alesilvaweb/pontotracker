package com.timesheetsystem.web.rest;

import static com.timesheetsystem.domain.TimesheetAsserts.*;
import static com.timesheetsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static com.timesheetsystem.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timesheetsystem.IntegrationTest;
import com.timesheetsystem.domain.Company;
import com.timesheetsystem.domain.Timesheet;
import com.timesheetsystem.domain.User;
import com.timesheetsystem.domain.enumeration.WorkModality;
import com.timesheetsystem.repository.TimesheetRepository;
import com.timesheetsystem.repository.UserRepository;
import com.timesheetsystem.service.TimesheetService;
import com.timesheetsystem.service.dto.TimesheetDTO;
import com.timesheetsystem.service.mapper.TimesheetMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TimesheetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TimesheetResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final WorkModality DEFAULT_MODALITY = WorkModality.REMOTE;
    private static final WorkModality UPDATED_MODALITY = WorkModality.IN_PERSON;

    private static final String DEFAULT_CLASSIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_CLASSIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_HOURS = 0D;
    private static final Double UPDATED_TOTAL_HOURS = 1D;
    private static final Double SMALLER_TOTAL_HOURS = 0D - 1D;

    private static final Double DEFAULT_OVERTIME_HOURS = 0D;
    private static final Double UPDATED_OVERTIME_HOURS = 1D;
    private static final Double SMALLER_OVERTIME_HOURS = 0D - 1D;

    private static final Double DEFAULT_ALLOWANCE_VALUE = 0D;
    private static final Double UPDATED_ALLOWANCE_VALUE = 1D;
    private static final Double SMALLER_ALLOWANCE_VALUE = 0D - 1D;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_APPROVED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_APPROVED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_APPROVED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_APPROVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_APPROVED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/timesheets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private TimesheetRepository timesheetRepositoryMock;

    @Autowired
    private TimesheetMapper timesheetMapper;

    @Mock
    private TimesheetService timesheetServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimesheetMockMvc;

    private Timesheet timesheet;

    private Timesheet insertedTimesheet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Timesheet createEntity(EntityManager em) {
        Timesheet timesheet = new Timesheet()
            .date(DEFAULT_DATE)
            .modality(DEFAULT_MODALITY)
            .classification(DEFAULT_CLASSIFICATION)
            .description(DEFAULT_DESCRIPTION)
            .totalHours(DEFAULT_TOTAL_HOURS)
            .overtimeHours(DEFAULT_OVERTIME_HOURS)
            .allowanceValue(DEFAULT_ALLOWANCE_VALUE)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .approvedAt(DEFAULT_APPROVED_AT)
            .approvedBy(DEFAULT_APPROVED_BY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        timesheet.setUser(user);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        timesheet.setCompany(company);
        return timesheet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Timesheet createUpdatedEntity(EntityManager em) {
        Timesheet timesheet = new Timesheet()
            .date(UPDATED_DATE)
            .modality(UPDATED_MODALITY)
            .classification(UPDATED_CLASSIFICATION)
            .description(UPDATED_DESCRIPTION)
            .totalHours(UPDATED_TOTAL_HOURS)
            .overtimeHours(UPDATED_OVERTIME_HOURS)
            .allowanceValue(UPDATED_ALLOWANCE_VALUE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .approvedBy(UPDATED_APPROVED_BY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        timesheet.setUser(user);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        timesheet.setCompany(company);
        return timesheet;
    }

    @BeforeEach
    public void initTest() {
        timesheet = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTimesheet != null) {
            timesheetRepository.delete(insertedTimesheet);
            insertedTimesheet = null;
        }
    }

    @Test
    @Transactional
    void createTimesheet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Timesheet
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);
        var returnedTimesheetDTO = om.readValue(
            restTimesheetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TimesheetDTO.class
        );

        // Validate the Timesheet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTimesheet = timesheetMapper.toEntity(returnedTimesheetDTO);
        assertTimesheetUpdatableFieldsEquals(returnedTimesheet, getPersistedTimesheet(returnedTimesheet));

        insertedTimesheet = returnedTimesheet;
    }

    @Test
    @Transactional
    void createTimesheetWithExistingId() throws Exception {
        // Create the Timesheet with an existing ID
        timesheet.setId(1L);
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimesheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Timesheet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheet.setDate(null);

        // Create the Timesheet, which fails.
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);

        restTimesheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModalityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheet.setModality(null);

        // Create the Timesheet, which fails.
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);

        restTimesheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalHoursIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheet.setTotalHours(null);

        // Create the Timesheet, which fails.
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);

        restTimesheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheet.setStatus(null);

        // Create the Timesheet, which fails.
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);

        restTimesheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheet.setCreatedAt(null);

        // Create the Timesheet, which fails.
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);

        restTimesheetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTimesheets() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList
        restTimesheetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timesheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].modality").value(hasItem(DEFAULT_MODALITY.toString())))
            .andExpect(jsonPath("$.[*].classification").value(hasItem(DEFAULT_CLASSIFICATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].totalHours").value(hasItem(DEFAULT_TOTAL_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].overtimeHours").value(hasItem(DEFAULT_OVERTIME_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].allowanceValue").value(hasItem(DEFAULT_ALLOWANCE_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(sameInstant(DEFAULT_APPROVED_AT))))
            .andExpect(jsonPath("$.[*].approvedBy").value(hasItem(DEFAULT_APPROVED_BY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTimesheetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(timesheetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTimesheetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(timesheetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTimesheetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(timesheetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTimesheetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(timesheetRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTimesheet() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get the timesheet
        restTimesheetMockMvc
            .perform(get(ENTITY_API_URL_ID, timesheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timesheet.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.modality").value(DEFAULT_MODALITY.toString()))
            .andExpect(jsonPath("$.classification").value(DEFAULT_CLASSIFICATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.totalHours").value(DEFAULT_TOTAL_HOURS.doubleValue()))
            .andExpect(jsonPath("$.overtimeHours").value(DEFAULT_OVERTIME_HOURS.doubleValue()))
            .andExpect(jsonPath("$.allowanceValue").value(DEFAULT_ALLOWANCE_VALUE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.approvedAt").value(sameInstant(DEFAULT_APPROVED_AT)))
            .andExpect(jsonPath("$.approvedBy").value(DEFAULT_APPROVED_BY));
    }

    @Test
    @Transactional
    void getTimesheetsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        Long id = timesheet.getId();

        defaultTimesheetFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTimesheetFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTimesheetFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTimesheetsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where date equals to
        defaultTimesheetFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where date in
        defaultTimesheetFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where date is not null
        defaultTimesheetFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where date is greater than or equal to
        defaultTimesheetFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where date is less than or equal to
        defaultTimesheetFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where date is less than
        defaultTimesheetFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where date is greater than
        defaultTimesheetFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetsByModalityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where modality equals to
        defaultTimesheetFiltering("modality.equals=" + DEFAULT_MODALITY, "modality.equals=" + UPDATED_MODALITY);
    }

    @Test
    @Transactional
    void getAllTimesheetsByModalityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where modality in
        defaultTimesheetFiltering("modality.in=" + DEFAULT_MODALITY + "," + UPDATED_MODALITY, "modality.in=" + UPDATED_MODALITY);
    }

    @Test
    @Transactional
    void getAllTimesheetsByModalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where modality is not null
        defaultTimesheetFiltering("modality.specified=true", "modality.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetsByClassificationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where classification equals to
        defaultTimesheetFiltering("classification.equals=" + DEFAULT_CLASSIFICATION, "classification.equals=" + UPDATED_CLASSIFICATION);
    }

    @Test
    @Transactional
    void getAllTimesheetsByClassificationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where classification in
        defaultTimesheetFiltering(
            "classification.in=" + DEFAULT_CLASSIFICATION + "," + UPDATED_CLASSIFICATION,
            "classification.in=" + UPDATED_CLASSIFICATION
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByClassificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where classification is not null
        defaultTimesheetFiltering("classification.specified=true", "classification.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetsByClassificationContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where classification contains
        defaultTimesheetFiltering("classification.contains=" + DEFAULT_CLASSIFICATION, "classification.contains=" + UPDATED_CLASSIFICATION);
    }

    @Test
    @Transactional
    void getAllTimesheetsByClassificationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where classification does not contain
        defaultTimesheetFiltering(
            "classification.doesNotContain=" + UPDATED_CLASSIFICATION,
            "classification.doesNotContain=" + DEFAULT_CLASSIFICATION
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where description equals to
        defaultTimesheetFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTimesheetsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where description in
        defaultTimesheetFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where description is not null
        defaultTimesheetFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where description contains
        defaultTimesheetFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTimesheetsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where description does not contain
        defaultTimesheetFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTimesheetsByTotalHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where totalHours equals to
        defaultTimesheetFiltering("totalHours.equals=" + DEFAULT_TOTAL_HOURS, "totalHours.equals=" + UPDATED_TOTAL_HOURS);
    }

    @Test
    @Transactional
    void getAllTimesheetsByTotalHoursIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where totalHours in
        defaultTimesheetFiltering(
            "totalHours.in=" + DEFAULT_TOTAL_HOURS + "," + UPDATED_TOTAL_HOURS,
            "totalHours.in=" + UPDATED_TOTAL_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByTotalHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where totalHours is not null
        defaultTimesheetFiltering("totalHours.specified=true", "totalHours.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetsByTotalHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where totalHours is greater than or equal to
        defaultTimesheetFiltering(
            "totalHours.greaterThanOrEqual=" + DEFAULT_TOTAL_HOURS,
            "totalHours.greaterThanOrEqual=" + UPDATED_TOTAL_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByTotalHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where totalHours is less than or equal to
        defaultTimesheetFiltering("totalHours.lessThanOrEqual=" + DEFAULT_TOTAL_HOURS, "totalHours.lessThanOrEqual=" + SMALLER_TOTAL_HOURS);
    }

    @Test
    @Transactional
    void getAllTimesheetsByTotalHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where totalHours is less than
        defaultTimesheetFiltering("totalHours.lessThan=" + UPDATED_TOTAL_HOURS, "totalHours.lessThan=" + DEFAULT_TOTAL_HOURS);
    }

    @Test
    @Transactional
    void getAllTimesheetsByTotalHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where totalHours is greater than
        defaultTimesheetFiltering("totalHours.greaterThan=" + SMALLER_TOTAL_HOURS, "totalHours.greaterThan=" + DEFAULT_TOTAL_HOURS);
    }

    @Test
    @Transactional
    void getAllTimesheetsByOvertimeHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where overtimeHours equals to
        defaultTimesheetFiltering("overtimeHours.equals=" + DEFAULT_OVERTIME_HOURS, "overtimeHours.equals=" + UPDATED_OVERTIME_HOURS);
    }

    @Test
    @Transactional
    void getAllTimesheetsByOvertimeHoursIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where overtimeHours in
        defaultTimesheetFiltering(
            "overtimeHours.in=" + DEFAULT_OVERTIME_HOURS + "," + UPDATED_OVERTIME_HOURS,
            "overtimeHours.in=" + UPDATED_OVERTIME_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByOvertimeHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where overtimeHours is not null
        defaultTimesheetFiltering("overtimeHours.specified=true", "overtimeHours.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetsByOvertimeHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where overtimeHours is greater than or equal to
        defaultTimesheetFiltering(
            "overtimeHours.greaterThanOrEqual=" + DEFAULT_OVERTIME_HOURS,
            "overtimeHours.greaterThanOrEqual=" + UPDATED_OVERTIME_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByOvertimeHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where overtimeHours is less than or equal to
        defaultTimesheetFiltering(
            "overtimeHours.lessThanOrEqual=" + DEFAULT_OVERTIME_HOURS,
            "overtimeHours.lessThanOrEqual=" + SMALLER_OVERTIME_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByOvertimeHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where overtimeHours is less than
        defaultTimesheetFiltering("overtimeHours.lessThan=" + UPDATED_OVERTIME_HOURS, "overtimeHours.lessThan=" + DEFAULT_OVERTIME_HOURS);
    }

    @Test
    @Transactional
    void getAllTimesheetsByOvertimeHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where overtimeHours is greater than
        defaultTimesheetFiltering(
            "overtimeHours.greaterThan=" + SMALLER_OVERTIME_HOURS,
            "overtimeHours.greaterThan=" + DEFAULT_OVERTIME_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByAllowanceValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where allowanceValue equals to
        defaultTimesheetFiltering("allowanceValue.equals=" + DEFAULT_ALLOWANCE_VALUE, "allowanceValue.equals=" + UPDATED_ALLOWANCE_VALUE);
    }

    @Test
    @Transactional
    void getAllTimesheetsByAllowanceValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where allowanceValue in
        defaultTimesheetFiltering(
            "allowanceValue.in=" + DEFAULT_ALLOWANCE_VALUE + "," + UPDATED_ALLOWANCE_VALUE,
            "allowanceValue.in=" + UPDATED_ALLOWANCE_VALUE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByAllowanceValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where allowanceValue is not null
        defaultTimesheetFiltering("allowanceValue.specified=true", "allowanceValue.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetsByAllowanceValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where allowanceValue is greater than or equal to
        defaultTimesheetFiltering(
            "allowanceValue.greaterThanOrEqual=" + DEFAULT_ALLOWANCE_VALUE,
            "allowanceValue.greaterThanOrEqual=" + UPDATED_ALLOWANCE_VALUE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByAllowanceValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where allowanceValue is less than or equal to
        defaultTimesheetFiltering(
            "allowanceValue.lessThanOrEqual=" + DEFAULT_ALLOWANCE_VALUE,
            "allowanceValue.lessThanOrEqual=" + SMALLER_ALLOWANCE_VALUE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByAllowanceValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where allowanceValue is less than
        defaultTimesheetFiltering(
            "allowanceValue.lessThan=" + UPDATED_ALLOWANCE_VALUE,
            "allowanceValue.lessThan=" + DEFAULT_ALLOWANCE_VALUE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByAllowanceValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where allowanceValue is greater than
        defaultTimesheetFiltering(
            "allowanceValue.greaterThan=" + SMALLER_ALLOWANCE_VALUE,
            "allowanceValue.greaterThan=" + DEFAULT_ALLOWANCE_VALUE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where status equals to
        defaultTimesheetFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTimesheetsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where status in
        defaultTimesheetFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTimesheetsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where status is not null
        defaultTimesheetFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetsByStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where status contains
        defaultTimesheetFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTimesheetsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where status does not contain
        defaultTimesheetFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllTimesheetsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where createdAt equals to
        defaultTimesheetFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where createdAt in
        defaultTimesheetFiltering("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT, "createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where createdAt is not null
        defaultTimesheetFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where createdAt is greater than or equal to
        defaultTimesheetFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where createdAt is less than or equal to
        defaultTimesheetFiltering("createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT, "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where createdAt is less than
        defaultTimesheetFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where createdAt is greater than
        defaultTimesheetFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where updatedAt equals to
        defaultTimesheetFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where updatedAt in
        defaultTimesheetFiltering("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT, "updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where updatedAt is not null
        defaultTimesheetFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetsByUpdatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where updatedAt is greater than or equal to
        defaultTimesheetFiltering(
            "updatedAt.greaterThanOrEqual=" + DEFAULT_UPDATED_AT,
            "updatedAt.greaterThanOrEqual=" + UPDATED_UPDATED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByUpdatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where updatedAt is less than or equal to
        defaultTimesheetFiltering("updatedAt.lessThanOrEqual=" + DEFAULT_UPDATED_AT, "updatedAt.lessThanOrEqual=" + SMALLER_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByUpdatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where updatedAt is less than
        defaultTimesheetFiltering("updatedAt.lessThan=" + UPDATED_UPDATED_AT, "updatedAt.lessThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByUpdatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where updatedAt is greater than
        defaultTimesheetFiltering("updatedAt.greaterThan=" + SMALLER_UPDATED_AT, "updatedAt.greaterThan=" + DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByApprovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedAt equals to
        defaultTimesheetFiltering("approvedAt.equals=" + DEFAULT_APPROVED_AT, "approvedAt.equals=" + UPDATED_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByApprovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedAt in
        defaultTimesheetFiltering(
            "approvedAt.in=" + DEFAULT_APPROVED_AT + "," + UPDATED_APPROVED_AT,
            "approvedAt.in=" + UPDATED_APPROVED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByApprovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedAt is not null
        defaultTimesheetFiltering("approvedAt.specified=true", "approvedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetsByApprovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedAt is greater than or equal to
        defaultTimesheetFiltering(
            "approvedAt.greaterThanOrEqual=" + DEFAULT_APPROVED_AT,
            "approvedAt.greaterThanOrEqual=" + UPDATED_APPROVED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByApprovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedAt is less than or equal to
        defaultTimesheetFiltering("approvedAt.lessThanOrEqual=" + DEFAULT_APPROVED_AT, "approvedAt.lessThanOrEqual=" + SMALLER_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByApprovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedAt is less than
        defaultTimesheetFiltering("approvedAt.lessThan=" + UPDATED_APPROVED_AT, "approvedAt.lessThan=" + DEFAULT_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByApprovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedAt is greater than
        defaultTimesheetFiltering("approvedAt.greaterThan=" + SMALLER_APPROVED_AT, "approvedAt.greaterThan=" + DEFAULT_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetsByApprovedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedBy equals to
        defaultTimesheetFiltering("approvedBy.equals=" + DEFAULT_APPROVED_BY, "approvedBy.equals=" + UPDATED_APPROVED_BY);
    }

    @Test
    @Transactional
    void getAllTimesheetsByApprovedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedBy in
        defaultTimesheetFiltering(
            "approvedBy.in=" + DEFAULT_APPROVED_BY + "," + UPDATED_APPROVED_BY,
            "approvedBy.in=" + UPDATED_APPROVED_BY
        );
    }

    @Test
    @Transactional
    void getAllTimesheetsByApprovedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedBy is not null
        defaultTimesheetFiltering("approvedBy.specified=true", "approvedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetsByApprovedByContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedBy contains
        defaultTimesheetFiltering("approvedBy.contains=" + DEFAULT_APPROVED_BY, "approvedBy.contains=" + UPDATED_APPROVED_BY);
    }

    @Test
    @Transactional
    void getAllTimesheetsByApprovedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheetList where approvedBy does not contain
        defaultTimesheetFiltering("approvedBy.doesNotContain=" + UPDATED_APPROVED_BY, "approvedBy.doesNotContain=" + DEFAULT_APPROVED_BY);
    }

    @Test
    @Transactional
    void getAllTimesheetsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            timesheetRepository.saveAndFlush(timesheet);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        timesheet.setUser(user);
        timesheetRepository.saveAndFlush(timesheet);
        Long userId = user.getId();
        // Get all the timesheetList where user equals to userId
        defaultTimesheetShouldBeFound("userId.equals=" + userId);

        // Get all the timesheetList where user equals to (userId + 1)
        defaultTimesheetShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllTimesheetsByCompanyIsEqualToSomething() throws Exception {
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            timesheetRepository.saveAndFlush(timesheet);
            company = CompanyResourceIT.createEntity(em);
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(company);
        em.flush();
        timesheet.setCompany(company);
        timesheetRepository.saveAndFlush(timesheet);
        Long companyId = company.getId();
        // Get all the timesheetList where company equals to companyId
        defaultTimesheetShouldBeFound("companyId.equals=" + companyId);

        // Get all the timesheetList where company equals to (companyId + 1)
        defaultTimesheetShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    private void defaultTimesheetFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTimesheetShouldBeFound(shouldBeFound);
        defaultTimesheetShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTimesheetShouldBeFound(String filter) throws Exception {
        restTimesheetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timesheet.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].modality").value(hasItem(DEFAULT_MODALITY.toString())))
            .andExpect(jsonPath("$.[*].classification").value(hasItem(DEFAULT_CLASSIFICATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].totalHours").value(hasItem(DEFAULT_TOTAL_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].overtimeHours").value(hasItem(DEFAULT_OVERTIME_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].allowanceValue").value(hasItem(DEFAULT_ALLOWANCE_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(sameInstant(DEFAULT_APPROVED_AT))))
            .andExpect(jsonPath("$.[*].approvedBy").value(hasItem(DEFAULT_APPROVED_BY)));

        // Check, that the count call also returns 1
        restTimesheetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTimesheetShouldNotBeFound(String filter) throws Exception {
        restTimesheetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTimesheetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTimesheet() throws Exception {
        // Get the timesheet
        restTimesheetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimesheet() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timesheet
        Timesheet updatedTimesheet = timesheetRepository.findById(timesheet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTimesheet are not directly saved in db
        em.detach(updatedTimesheet);
        updatedTimesheet
            .date(UPDATED_DATE)
            .modality(UPDATED_MODALITY)
            .classification(UPDATED_CLASSIFICATION)
            .description(UPDATED_DESCRIPTION)
            .totalHours(UPDATED_TOTAL_HOURS)
            .overtimeHours(UPDATED_OVERTIME_HOURS)
            .allowanceValue(UPDATED_ALLOWANCE_VALUE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .approvedBy(UPDATED_APPROVED_BY);
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(updatedTimesheet);

        restTimesheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timesheetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timesheetDTO))
            )
            .andExpect(status().isOk());

        // Validate the Timesheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTimesheetToMatchAllProperties(updatedTimesheet);
    }

    @Test
    @Transactional
    void putNonExistingTimesheet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheet.setId(longCount.incrementAndGet());

        // Create the Timesheet
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimesheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timesheetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timesheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timesheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimesheet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheet.setId(longCount.incrementAndGet());

        // Create the Timesheet
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timesheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timesheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimesheet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheet.setId(longCount.incrementAndGet());

        // Create the Timesheet
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Timesheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimesheetWithPatch() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timesheet using partial update
        Timesheet partialUpdatedTimesheet = new Timesheet();
        partialUpdatedTimesheet.setId(timesheet.getId());

        partialUpdatedTimesheet
            .modality(UPDATED_MODALITY)
            .description(UPDATED_DESCRIPTION)
            .allowanceValue(UPDATED_ALLOWANCE_VALUE)
            .status(UPDATED_STATUS);

        restTimesheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimesheet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimesheet))
            )
            .andExpect(status().isOk());

        // Validate the Timesheet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimesheetUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTimesheet, timesheet),
            getPersistedTimesheet(timesheet)
        );
    }

    @Test
    @Transactional
    void fullUpdateTimesheetWithPatch() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timesheet using partial update
        Timesheet partialUpdatedTimesheet = new Timesheet();
        partialUpdatedTimesheet.setId(timesheet.getId());

        partialUpdatedTimesheet
            .date(UPDATED_DATE)
            .modality(UPDATED_MODALITY)
            .classification(UPDATED_CLASSIFICATION)
            .description(UPDATED_DESCRIPTION)
            .totalHours(UPDATED_TOTAL_HOURS)
            .overtimeHours(UPDATED_OVERTIME_HOURS)
            .allowanceValue(UPDATED_ALLOWANCE_VALUE)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .approvedBy(UPDATED_APPROVED_BY);

        restTimesheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimesheet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimesheet))
            )
            .andExpect(status().isOk());

        // Validate the Timesheet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimesheetUpdatableFieldsEquals(partialUpdatedTimesheet, getPersistedTimesheet(partialUpdatedTimesheet));
    }

    @Test
    @Transactional
    void patchNonExistingTimesheet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheet.setId(longCount.incrementAndGet());

        // Create the Timesheet
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimesheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timesheetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timesheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timesheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimesheet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheet.setId(longCount.incrementAndGet());

        // Create the Timesheet
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timesheetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Timesheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimesheet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheet.setId(longCount.incrementAndGet());

        // Create the Timesheet
        TimesheetDTO timesheetDTO = timesheetMapper.toDto(timesheet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(timesheetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Timesheet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimesheet() throws Exception {
        // Initialize the database
        insertedTimesheet = timesheetRepository.saveAndFlush(timesheet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the timesheet
        restTimesheetMockMvc
            .perform(delete(ENTITY_API_URL_ID, timesheet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return timesheetRepository.count();
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

    protected Timesheet getPersistedTimesheet(Timesheet timesheet) {
        return timesheetRepository.findById(timesheet.getId()).orElseThrow();
    }

    protected void assertPersistedTimesheetToMatchAllProperties(Timesheet expectedTimesheet) {
        assertTimesheetAllPropertiesEquals(expectedTimesheet, getPersistedTimesheet(expectedTimesheet));
    }

    protected void assertPersistedTimesheetToMatchUpdatableProperties(Timesheet expectedTimesheet) {
        assertTimesheetAllUpdatablePropertiesEquals(expectedTimesheet, getPersistedTimesheet(expectedTimesheet));
    }
}
