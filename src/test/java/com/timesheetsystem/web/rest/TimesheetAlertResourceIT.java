package com.timesheetsystem.web.rest;

import static com.timesheetsystem.domain.TimesheetAlertAsserts.*;
import static com.timesheetsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static com.timesheetsystem.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timesheetsystem.IntegrationTest;
import com.timesheetsystem.domain.TimesheetAlert;
import com.timesheetsystem.domain.User;
import com.timesheetsystem.repository.TimesheetAlertRepository;
import com.timesheetsystem.repository.UserRepository;
import com.timesheetsystem.service.TimesheetAlertService;
import com.timesheetsystem.service.dto.TimesheetAlertDTO;
import com.timesheetsystem.service.mapper.TimesheetAlertMapper;
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
 * Integration tests for the {@link TimesheetAlertResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TimesheetAlertResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final Long DEFAULT_TIMESHEET_ID = 1L;
    private static final Long UPDATED_TIMESHEET_ID = 2L;
    private static final Long SMALLER_TIMESHEET_ID = 1L - 1L;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_SEVERITY = "AAAAAAAAAA";
    private static final String UPDATED_SEVERITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_RESOLVED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RESOLVED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_RESOLVED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_RESOLUTION = "AAAAAAAAAA";
    private static final String UPDATED_RESOLUTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/timesheet-alerts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TimesheetAlertRepository timesheetAlertRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private TimesheetAlertRepository timesheetAlertRepositoryMock;

    @Autowired
    private TimesheetAlertMapper timesheetAlertMapper;

    @Mock
    private TimesheetAlertService timesheetAlertServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimesheetAlertMockMvc;

    private TimesheetAlert timesheetAlert;

    private TimesheetAlert insertedTimesheetAlert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimesheetAlert createEntity(EntityManager em) {
        TimesheetAlert timesheetAlert = new TimesheetAlert()
            .userId(DEFAULT_USER_ID)
            .timesheetId(DEFAULT_TIMESHEET_ID)
            .date(DEFAULT_DATE)
            .type(DEFAULT_TYPE)
            .message(DEFAULT_MESSAGE)
            .severity(DEFAULT_SEVERITY)
            .status(DEFAULT_STATUS)
            .createdAt(DEFAULT_CREATED_AT)
            .resolvedAt(DEFAULT_RESOLVED_AT)
            .resolution(DEFAULT_RESOLUTION);
        return timesheetAlert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimesheetAlert createUpdatedEntity(EntityManager em) {
        TimesheetAlert timesheetAlert = new TimesheetAlert()
            .userId(UPDATED_USER_ID)
            .timesheetId(UPDATED_TIMESHEET_ID)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .message(UPDATED_MESSAGE)
            .severity(UPDATED_SEVERITY)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT)
            .resolution(UPDATED_RESOLUTION);
        return timesheetAlert;
    }

    @BeforeEach
    public void initTest() {
        timesheetAlert = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTimesheetAlert != null) {
            timesheetAlertRepository.delete(insertedTimesheetAlert);
            insertedTimesheetAlert = null;
        }
    }

    @Test
    @Transactional
    void createTimesheetAlert() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TimesheetAlert
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);
        var returnedTimesheetAlertDTO = om.readValue(
            restTimesheetAlertMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAlertDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TimesheetAlertDTO.class
        );

        // Validate the TimesheetAlert in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTimesheetAlert = timesheetAlertMapper.toEntity(returnedTimesheetAlertDTO);
        assertTimesheetAlertUpdatableFieldsEquals(returnedTimesheetAlert, getPersistedTimesheetAlert(returnedTimesheetAlert));

        insertedTimesheetAlert = returnedTimesheetAlert;
    }

    @Test
    @Transactional
    void createTimesheetAlertWithExistingId() throws Exception {
        // Create the TimesheetAlert with an existing ID
        timesheetAlert.setId(1L);
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimesheetAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAlertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TimesheetAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAlert.setUserId(null);

        // Create the TimesheetAlert, which fails.
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        restTimesheetAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimesheetIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAlert.setTimesheetId(null);

        // Create the TimesheetAlert, which fails.
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        restTimesheetAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAlert.setDate(null);

        // Create the TimesheetAlert, which fails.
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        restTimesheetAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAlert.setType(null);

        // Create the TimesheetAlert, which fails.
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        restTimesheetAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMessageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAlert.setMessage(null);

        // Create the TimesheetAlert, which fails.
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        restTimesheetAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeverityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAlert.setSeverity(null);

        // Create the TimesheetAlert, which fails.
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        restTimesheetAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAlert.setStatus(null);

        // Create the TimesheetAlert, which fails.
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        restTimesheetAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetAlert.setCreatedAt(null);

        // Create the TimesheetAlert, which fails.
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        restTimesheetAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTimesheetAlerts() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList
        restTimesheetAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timesheetAlert.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].timesheetId").value(hasItem(DEFAULT_TIMESHEET_ID.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].resolvedAt").value(hasItem(sameInstant(DEFAULT_RESOLVED_AT))))
            .andExpect(jsonPath("$.[*].resolution").value(hasItem(DEFAULT_RESOLUTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTimesheetAlertsWithEagerRelationshipsIsEnabled() throws Exception {
        when(timesheetAlertServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTimesheetAlertMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(timesheetAlertServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTimesheetAlertsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(timesheetAlertServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTimesheetAlertMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(timesheetAlertRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTimesheetAlert() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get the timesheetAlert
        restTimesheetAlertMockMvc
            .perform(get(ENTITY_API_URL_ID, timesheetAlert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timesheetAlert.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.timesheetId").value(DEFAULT_TIMESHEET_ID.intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.severity").value(DEFAULT_SEVERITY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.resolvedAt").value(sameInstant(DEFAULT_RESOLVED_AT)))
            .andExpect(jsonPath("$.resolution").value(DEFAULT_RESOLUTION));
    }

    @Test
    @Transactional
    void getTimesheetAlertsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        Long id = timesheetAlert.getId();

        defaultTimesheetAlertFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTimesheetAlertFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTimesheetAlertFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where userId equals to
        defaultTimesheetAlertFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where userId in
        defaultTimesheetAlertFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where userId is not null
        defaultTimesheetAlertFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where userId is greater than or equal to
        defaultTimesheetAlertFiltering("userId.greaterThanOrEqual=" + DEFAULT_USER_ID, "userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where userId is less than or equal to
        defaultTimesheetAlertFiltering("userId.lessThanOrEqual=" + DEFAULT_USER_ID, "userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where userId is less than
        defaultTimesheetAlertFiltering("userId.lessThan=" + UPDATED_USER_ID, "userId.lessThan=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where userId is greater than
        defaultTimesheetAlertFiltering("userId.greaterThan=" + SMALLER_USER_ID, "userId.greaterThan=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByTimesheetIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where timesheetId equals to
        defaultTimesheetAlertFiltering("timesheetId.equals=" + DEFAULT_TIMESHEET_ID, "timesheetId.equals=" + UPDATED_TIMESHEET_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByTimesheetIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where timesheetId in
        defaultTimesheetAlertFiltering(
            "timesheetId.in=" + DEFAULT_TIMESHEET_ID + "," + UPDATED_TIMESHEET_ID,
            "timesheetId.in=" + UPDATED_TIMESHEET_ID
        );
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByTimesheetIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where timesheetId is not null
        defaultTimesheetAlertFiltering("timesheetId.specified=true", "timesheetId.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByTimesheetIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where timesheetId is greater than or equal to
        defaultTimesheetAlertFiltering(
            "timesheetId.greaterThanOrEqual=" + DEFAULT_TIMESHEET_ID,
            "timesheetId.greaterThanOrEqual=" + UPDATED_TIMESHEET_ID
        );
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByTimesheetIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where timesheetId is less than or equal to
        defaultTimesheetAlertFiltering(
            "timesheetId.lessThanOrEqual=" + DEFAULT_TIMESHEET_ID,
            "timesheetId.lessThanOrEqual=" + SMALLER_TIMESHEET_ID
        );
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByTimesheetIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where timesheetId is less than
        defaultTimesheetAlertFiltering("timesheetId.lessThan=" + UPDATED_TIMESHEET_ID, "timesheetId.lessThan=" + DEFAULT_TIMESHEET_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByTimesheetIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where timesheetId is greater than
        defaultTimesheetAlertFiltering(
            "timesheetId.greaterThan=" + SMALLER_TIMESHEET_ID,
            "timesheetId.greaterThan=" + DEFAULT_TIMESHEET_ID
        );
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where date equals to
        defaultTimesheetAlertFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where date in
        defaultTimesheetAlertFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where date is not null
        defaultTimesheetAlertFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where date is greater than or equal to
        defaultTimesheetAlertFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where date is less than or equal to
        defaultTimesheetAlertFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where date is less than
        defaultTimesheetAlertFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where date is greater than
        defaultTimesheetAlertFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where type equals to
        defaultTimesheetAlertFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where type in
        defaultTimesheetAlertFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where type is not null
        defaultTimesheetAlertFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where type contains
        defaultTimesheetAlertFiltering("type.contains=" + DEFAULT_TYPE, "type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where type does not contain
        defaultTimesheetAlertFiltering("type.doesNotContain=" + UPDATED_TYPE, "type.doesNotContain=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where message equals to
        defaultTimesheetAlertFiltering("message.equals=" + DEFAULT_MESSAGE, "message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where message in
        defaultTimesheetAlertFiltering("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE, "message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where message is not null
        defaultTimesheetAlertFiltering("message.specified=true", "message.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByMessageContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where message contains
        defaultTimesheetAlertFiltering("message.contains=" + DEFAULT_MESSAGE, "message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where message does not contain
        defaultTimesheetAlertFiltering("message.doesNotContain=" + UPDATED_MESSAGE, "message.doesNotContain=" + DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsBySeverityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where severity equals to
        defaultTimesheetAlertFiltering("severity.equals=" + DEFAULT_SEVERITY, "severity.equals=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsBySeverityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where severity in
        defaultTimesheetAlertFiltering("severity.in=" + DEFAULT_SEVERITY + "," + UPDATED_SEVERITY, "severity.in=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsBySeverityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where severity is not null
        defaultTimesheetAlertFiltering("severity.specified=true", "severity.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsBySeverityContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where severity contains
        defaultTimesheetAlertFiltering("severity.contains=" + DEFAULT_SEVERITY, "severity.contains=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsBySeverityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where severity does not contain
        defaultTimesheetAlertFiltering("severity.doesNotContain=" + UPDATED_SEVERITY, "severity.doesNotContain=" + DEFAULT_SEVERITY);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where status equals to
        defaultTimesheetAlertFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where status in
        defaultTimesheetAlertFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where status is not null
        defaultTimesheetAlertFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where status contains
        defaultTimesheetAlertFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where status does not contain
        defaultTimesheetAlertFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where createdAt equals to
        defaultTimesheetAlertFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where createdAt in
        defaultTimesheetAlertFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where createdAt is not null
        defaultTimesheetAlertFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where createdAt is greater than or equal to
        defaultTimesheetAlertFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where createdAt is less than or equal to
        defaultTimesheetAlertFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where createdAt is less than
        defaultTimesheetAlertFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where createdAt is greater than
        defaultTimesheetAlertFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolvedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where resolvedAt equals to
        defaultTimesheetAlertFiltering("resolvedAt.equals=" + DEFAULT_RESOLVED_AT, "resolvedAt.equals=" + UPDATED_RESOLVED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolvedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where resolvedAt in
        defaultTimesheetAlertFiltering(
            "resolvedAt.in=" + DEFAULT_RESOLVED_AT + "," + UPDATED_RESOLVED_AT,
            "resolvedAt.in=" + UPDATED_RESOLVED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolvedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where resolvedAt is not null
        defaultTimesheetAlertFiltering("resolvedAt.specified=true", "resolvedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolvedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where resolvedAt is greater than or equal to
        defaultTimesheetAlertFiltering(
            "resolvedAt.greaterThanOrEqual=" + DEFAULT_RESOLVED_AT,
            "resolvedAt.greaterThanOrEqual=" + UPDATED_RESOLVED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolvedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where resolvedAt is less than or equal to
        defaultTimesheetAlertFiltering(
            "resolvedAt.lessThanOrEqual=" + DEFAULT_RESOLVED_AT,
            "resolvedAt.lessThanOrEqual=" + SMALLER_RESOLVED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolvedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where resolvedAt is less than
        defaultTimesheetAlertFiltering("resolvedAt.lessThan=" + UPDATED_RESOLVED_AT, "resolvedAt.lessThan=" + DEFAULT_RESOLVED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolvedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where resolvedAt is greater than
        defaultTimesheetAlertFiltering("resolvedAt.greaterThan=" + SMALLER_RESOLVED_AT, "resolvedAt.greaterThan=" + DEFAULT_RESOLVED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolutionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where resolution equals to
        defaultTimesheetAlertFiltering("resolution.equals=" + DEFAULT_RESOLUTION, "resolution.equals=" + UPDATED_RESOLUTION);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolutionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where resolution in
        defaultTimesheetAlertFiltering(
            "resolution.in=" + DEFAULT_RESOLUTION + "," + UPDATED_RESOLUTION,
            "resolution.in=" + UPDATED_RESOLUTION
        );
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolutionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where resolution is not null
        defaultTimesheetAlertFiltering("resolution.specified=true", "resolution.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolutionContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where resolution contains
        defaultTimesheetAlertFiltering("resolution.contains=" + DEFAULT_RESOLUTION, "resolution.contains=" + UPDATED_RESOLUTION);
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolutionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        // Get all the timesheetAlertList where resolution does not contain
        defaultTimesheetAlertFiltering(
            "resolution.doesNotContain=" + UPDATED_RESOLUTION,
            "resolution.doesNotContain=" + DEFAULT_RESOLUTION
        );
    }

    @Test
    @Transactional
    void getAllTimesheetAlertsByResolvedByIsEqualToSomething() throws Exception {
        User resolvedBy;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            timesheetAlertRepository.saveAndFlush(timesheetAlert);
            resolvedBy = UserResourceIT.createEntity(em);
        } else {
            resolvedBy = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(resolvedBy);
        em.flush();
        timesheetAlert.setResolvedBy(resolvedBy);
        timesheetAlertRepository.saveAndFlush(timesheetAlert);
        Long resolvedById = resolvedBy.getId();
        // Get all the timesheetAlertList where resolvedBy equals to resolvedById
        defaultTimesheetAlertShouldBeFound("resolvedById.equals=" + resolvedById);

        // Get all the timesheetAlertList where resolvedBy equals to (resolvedById + 1)
        defaultTimesheetAlertShouldNotBeFound("resolvedById.equals=" + (resolvedById + 1));
    }

    private void defaultTimesheetAlertFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTimesheetAlertShouldBeFound(shouldBeFound);
        defaultTimesheetAlertShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTimesheetAlertShouldBeFound(String filter) throws Exception {
        restTimesheetAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timesheetAlert.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].timesheetId").value(hasItem(DEFAULT_TIMESHEET_ID.intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].resolvedAt").value(hasItem(sameInstant(DEFAULT_RESOLVED_AT))))
            .andExpect(jsonPath("$.[*].resolution").value(hasItem(DEFAULT_RESOLUTION)));

        // Check, that the count call also returns 1
        restTimesheetAlertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTimesheetAlertShouldNotBeFound(String filter) throws Exception {
        restTimesheetAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTimesheetAlertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTimesheetAlert() throws Exception {
        // Get the timesheetAlert
        restTimesheetAlertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimesheetAlert() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timesheetAlert
        TimesheetAlert updatedTimesheetAlert = timesheetAlertRepository.findById(timesheetAlert.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTimesheetAlert are not directly saved in db
        em.detach(updatedTimesheetAlert);
        updatedTimesheetAlert
            .userId(UPDATED_USER_ID)
            .timesheetId(UPDATED_TIMESHEET_ID)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .message(UPDATED_MESSAGE)
            .severity(UPDATED_SEVERITY)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT)
            .resolution(UPDATED_RESOLUTION);
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(updatedTimesheetAlert);

        restTimesheetAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timesheetAlertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timesheetAlertDTO))
            )
            .andExpect(status().isOk());

        // Validate the TimesheetAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTimesheetAlertToMatchAllProperties(updatedTimesheetAlert);
    }

    @Test
    @Transactional
    void putNonExistingTimesheetAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetAlert.setId(longCount.incrementAndGet());

        // Create the TimesheetAlert
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimesheetAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timesheetAlertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timesheetAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimesheetAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimesheetAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetAlert.setId(longCount.incrementAndGet());

        // Create the TimesheetAlert
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timesheetAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimesheetAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimesheetAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetAlert.setId(longCount.incrementAndGet());

        // Create the TimesheetAlert
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetAlertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetAlertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimesheetAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimesheetAlertWithPatch() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timesheetAlert using partial update
        TimesheetAlert partialUpdatedTimesheetAlert = new TimesheetAlert();
        partialUpdatedTimesheetAlert.setId(timesheetAlert.getId());

        partialUpdatedTimesheetAlert
            .timesheetId(UPDATED_TIMESHEET_ID)
            .date(UPDATED_DATE)
            .message(UPDATED_MESSAGE)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT)
            .resolution(UPDATED_RESOLUTION);

        restTimesheetAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimesheetAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimesheetAlert))
            )
            .andExpect(status().isOk());

        // Validate the TimesheetAlert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimesheetAlertUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTimesheetAlert, timesheetAlert),
            getPersistedTimesheetAlert(timesheetAlert)
        );
    }

    @Test
    @Transactional
    void fullUpdateTimesheetAlertWithPatch() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timesheetAlert using partial update
        TimesheetAlert partialUpdatedTimesheetAlert = new TimesheetAlert();
        partialUpdatedTimesheetAlert.setId(timesheetAlert.getId());

        partialUpdatedTimesheetAlert
            .userId(UPDATED_USER_ID)
            .timesheetId(UPDATED_TIMESHEET_ID)
            .date(UPDATED_DATE)
            .type(UPDATED_TYPE)
            .message(UPDATED_MESSAGE)
            .severity(UPDATED_SEVERITY)
            .status(UPDATED_STATUS)
            .createdAt(UPDATED_CREATED_AT)
            .resolvedAt(UPDATED_RESOLVED_AT)
            .resolution(UPDATED_RESOLUTION);

        restTimesheetAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimesheetAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimesheetAlert))
            )
            .andExpect(status().isOk());

        // Validate the TimesheetAlert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimesheetAlertUpdatableFieldsEquals(partialUpdatedTimesheetAlert, getPersistedTimesheetAlert(partialUpdatedTimesheetAlert));
    }

    @Test
    @Transactional
    void patchNonExistingTimesheetAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetAlert.setId(longCount.incrementAndGet());

        // Create the TimesheetAlert
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimesheetAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timesheetAlertDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timesheetAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimesheetAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimesheetAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetAlert.setId(longCount.incrementAndGet());

        // Create the TimesheetAlert
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timesheetAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimesheetAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimesheetAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetAlert.setId(longCount.incrementAndGet());

        // Create the TimesheetAlert
        TimesheetAlertDTO timesheetAlertDTO = timesheetAlertMapper.toDto(timesheetAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetAlertMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(timesheetAlertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimesheetAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimesheetAlert() throws Exception {
        // Initialize the database
        insertedTimesheetAlert = timesheetAlertRepository.saveAndFlush(timesheetAlert);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the timesheetAlert
        restTimesheetAlertMockMvc
            .perform(delete(ENTITY_API_URL_ID, timesheetAlert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return timesheetAlertRepository.count();
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

    protected TimesheetAlert getPersistedTimesheetAlert(TimesheetAlert timesheetAlert) {
        return timesheetAlertRepository.findById(timesheetAlert.getId()).orElseThrow();
    }

    protected void assertPersistedTimesheetAlertToMatchAllProperties(TimesheetAlert expectedTimesheetAlert) {
        assertTimesheetAlertAllPropertiesEquals(expectedTimesheetAlert, getPersistedTimesheetAlert(expectedTimesheetAlert));
    }

    protected void assertPersistedTimesheetAlertToMatchUpdatableProperties(TimesheetAlert expectedTimesheetAlert) {
        assertTimesheetAlertAllUpdatablePropertiesEquals(expectedTimesheetAlert, getPersistedTimesheetAlert(expectedTimesheetAlert));
    }
}
