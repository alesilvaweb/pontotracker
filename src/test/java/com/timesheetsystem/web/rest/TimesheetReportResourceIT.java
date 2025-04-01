package com.timesheetsystem.web.rest;

import static com.timesheetsystem.domain.TimesheetReportAsserts.*;
import static com.timesheetsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static com.timesheetsystem.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timesheetsystem.IntegrationTest;
import com.timesheetsystem.domain.TimesheetReport;
import com.timesheetsystem.domain.User;
import com.timesheetsystem.repository.TimesheetReportRepository;
import com.timesheetsystem.repository.UserRepository;
import com.timesheetsystem.service.TimesheetReportService;
import com.timesheetsystem.service.dto.TimesheetReportDTO;
import com.timesheetsystem.service.mapper.TimesheetReportMapper;
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
 * Integration tests for the {@link TimesheetReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TimesheetReportResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;
    private static final Long SMALLER_USER_ID = 1L - 1L;

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_COMPANY_ID = 1L;
    private static final Long UPDATED_COMPANY_ID = 2L;
    private static final Long SMALLER_COMPANY_ID = 1L - 1L;

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_TOTAL_REGULAR_HOURS = 0D;
    private static final Double UPDATED_TOTAL_REGULAR_HOURS = 1D;
    private static final Double SMALLER_TOTAL_REGULAR_HOURS = 0D - 1D;

    private static final Double DEFAULT_TOTAL_OVERTIME_HOURS = 0D;
    private static final Double UPDATED_TOTAL_OVERTIME_HOURS = 1D;
    private static final Double SMALLER_TOTAL_OVERTIME_HOURS = 0D - 1D;

    private static final Double DEFAULT_TOTAL_ALLOWANCE = 0D;
    private static final Double UPDATED_TOTAL_ALLOWANCE = 1D;
    private static final Double SMALLER_TOTAL_ALLOWANCE = 0D - 1D;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_GENERATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_GENERATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_GENERATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_APPROVED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_APPROVED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_APPROVED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/timesheet-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TimesheetReportRepository timesheetReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private TimesheetReportRepository timesheetReportRepositoryMock;

    @Autowired
    private TimesheetReportMapper timesheetReportMapper;

    @Mock
    private TimesheetReportService timesheetReportServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimesheetReportMockMvc;

    private TimesheetReport timesheetReport;

    private TimesheetReport insertedTimesheetReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimesheetReport createEntity(EntityManager em) {
        TimesheetReport timesheetReport = new TimesheetReport()
            .userId(DEFAULT_USER_ID)
            .userName(DEFAULT_USER_NAME)
            .companyId(DEFAULT_COMPANY_ID)
            .companyName(DEFAULT_COMPANY_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .totalRegularHours(DEFAULT_TOTAL_REGULAR_HOURS)
            .totalOvertimeHours(DEFAULT_TOTAL_OVERTIME_HOURS)
            .totalAllowance(DEFAULT_TOTAL_ALLOWANCE)
            .status(DEFAULT_STATUS)
            .generatedAt(DEFAULT_GENERATED_AT)
            .approvedAt(DEFAULT_APPROVED_AT)
            .comments(DEFAULT_COMMENTS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        timesheetReport.setGeneratedBy(user);
        return timesheetReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimesheetReport createUpdatedEntity(EntityManager em) {
        TimesheetReport timesheetReport = new TimesheetReport()
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME)
            .companyId(UPDATED_COMPANY_ID)
            .companyName(UPDATED_COMPANY_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .totalRegularHours(UPDATED_TOTAL_REGULAR_HOURS)
            .totalOvertimeHours(UPDATED_TOTAL_OVERTIME_HOURS)
            .totalAllowance(UPDATED_TOTAL_ALLOWANCE)
            .status(UPDATED_STATUS)
            .generatedAt(UPDATED_GENERATED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .comments(UPDATED_COMMENTS);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        timesheetReport.setGeneratedBy(user);
        return timesheetReport;
    }

    @BeforeEach
    public void initTest() {
        timesheetReport = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTimesheetReport != null) {
            timesheetReportRepository.delete(insertedTimesheetReport);
            insertedTimesheetReport = null;
        }
    }

    @Test
    @Transactional
    void createTimesheetReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TimesheetReport
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);
        var returnedTimesheetReportDTO = om.readValue(
            restTimesheetReportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TimesheetReportDTO.class
        );

        // Validate the TimesheetReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTimesheetReport = timesheetReportMapper.toEntity(returnedTimesheetReportDTO);
        assertTimesheetReportUpdatableFieldsEquals(returnedTimesheetReport, getPersistedTimesheetReport(returnedTimesheetReport));

        insertedTimesheetReport = returnedTimesheetReport;
    }

    @Test
    @Transactional
    void createTimesheetReportWithExistingId() throws Exception {
        // Create the TimesheetReport with an existing ID
        timesheetReport.setId(1L);
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimesheetReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TimesheetReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetReport.setUserId(null);

        // Create the TimesheetReport, which fails.
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        restTimesheetReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetReport.setUserName(null);

        // Create the TimesheetReport, which fails.
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        restTimesheetReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompanyIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetReport.setCompanyId(null);

        // Create the TimesheetReport, which fails.
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        restTimesheetReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompanyNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetReport.setCompanyName(null);

        // Create the TimesheetReport, which fails.
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        restTimesheetReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetReport.setStartDate(null);

        // Create the TimesheetReport, which fails.
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        restTimesheetReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetReport.setEndDate(null);

        // Create the TimesheetReport, which fails.
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        restTimesheetReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalRegularHoursIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetReport.setTotalRegularHours(null);

        // Create the TimesheetReport, which fails.
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        restTimesheetReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalOvertimeHoursIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetReport.setTotalOvertimeHours(null);

        // Create the TimesheetReport, which fails.
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        restTimesheetReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAllowanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetReport.setTotalAllowance(null);

        // Create the TimesheetReport, which fails.
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        restTimesheetReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetReport.setStatus(null);

        // Create the TimesheetReport, which fails.
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        restTimesheetReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGeneratedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timesheetReport.setGeneratedAt(null);

        // Create the TimesheetReport, which fails.
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        restTimesheetReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTimesheetReports() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList
        restTimesheetReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timesheetReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalRegularHours").value(hasItem(DEFAULT_TOTAL_REGULAR_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].totalOvertimeHours").value(hasItem(DEFAULT_TOTAL_OVERTIME_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].totalAllowance").value(hasItem(DEFAULT_TOTAL_ALLOWANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].generatedAt").value(hasItem(sameInstant(DEFAULT_GENERATED_AT))))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(sameInstant(DEFAULT_APPROVED_AT))))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTimesheetReportsWithEagerRelationshipsIsEnabled() throws Exception {
        when(timesheetReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTimesheetReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(timesheetReportServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTimesheetReportsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(timesheetReportServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTimesheetReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(timesheetReportRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTimesheetReport() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get the timesheetReport
        restTimesheetReportMockMvc
            .perform(get(ENTITY_API_URL_ID, timesheetReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timesheetReport.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID.intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.totalRegularHours").value(DEFAULT_TOTAL_REGULAR_HOURS.doubleValue()))
            .andExpect(jsonPath("$.totalOvertimeHours").value(DEFAULT_TOTAL_OVERTIME_HOURS.doubleValue()))
            .andExpect(jsonPath("$.totalAllowance").value(DEFAULT_TOTAL_ALLOWANCE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.generatedAt").value(sameInstant(DEFAULT_GENERATED_AT)))
            .andExpect(jsonPath("$.approvedAt").value(sameInstant(DEFAULT_APPROVED_AT)))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getTimesheetReportsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        Long id = timesheetReport.getId();

        defaultTimesheetReportFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTimesheetReportFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTimesheetReportFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where userId equals to
        defaultTimesheetReportFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where userId in
        defaultTimesheetReportFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where userId is not null
        defaultTimesheetReportFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where userId is greater than or equal to
        defaultTimesheetReportFiltering("userId.greaterThanOrEqual=" + DEFAULT_USER_ID, "userId.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where userId is less than or equal to
        defaultTimesheetReportFiltering("userId.lessThanOrEqual=" + DEFAULT_USER_ID, "userId.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where userId is less than
        defaultTimesheetReportFiltering("userId.lessThan=" + UPDATED_USER_ID, "userId.lessThan=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where userId is greater than
        defaultTimesheetReportFiltering("userId.greaterThan=" + SMALLER_USER_ID, "userId.greaterThan=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByUserNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where userName equals to
        defaultTimesheetReportFiltering("userName.equals=" + DEFAULT_USER_NAME, "userName.equals=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByUserNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where userName in
        defaultTimesheetReportFiltering("userName.in=" + DEFAULT_USER_NAME + "," + UPDATED_USER_NAME, "userName.in=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByUserNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where userName is not null
        defaultTimesheetReportFiltering("userName.specified=true", "userName.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByUserNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where userName contains
        defaultTimesheetReportFiltering("userName.contains=" + DEFAULT_USER_NAME, "userName.contains=" + UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByUserNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where userName does not contain
        defaultTimesheetReportFiltering("userName.doesNotContain=" + UPDATED_USER_NAME, "userName.doesNotContain=" + DEFAULT_USER_NAME);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCompanyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where companyId equals to
        defaultTimesheetReportFiltering("companyId.equals=" + DEFAULT_COMPANY_ID, "companyId.equals=" + UPDATED_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCompanyIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where companyId in
        defaultTimesheetReportFiltering(
            "companyId.in=" + DEFAULT_COMPANY_ID + "," + UPDATED_COMPANY_ID,
            "companyId.in=" + UPDATED_COMPANY_ID
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCompanyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where companyId is not null
        defaultTimesheetReportFiltering("companyId.specified=true", "companyId.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCompanyIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where companyId is greater than or equal to
        defaultTimesheetReportFiltering(
            "companyId.greaterThanOrEqual=" + DEFAULT_COMPANY_ID,
            "companyId.greaterThanOrEqual=" + UPDATED_COMPANY_ID
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCompanyIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where companyId is less than or equal to
        defaultTimesheetReportFiltering(
            "companyId.lessThanOrEqual=" + DEFAULT_COMPANY_ID,
            "companyId.lessThanOrEqual=" + SMALLER_COMPANY_ID
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCompanyIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where companyId is less than
        defaultTimesheetReportFiltering("companyId.lessThan=" + UPDATED_COMPANY_ID, "companyId.lessThan=" + DEFAULT_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCompanyIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where companyId is greater than
        defaultTimesheetReportFiltering("companyId.greaterThan=" + SMALLER_COMPANY_ID, "companyId.greaterThan=" + DEFAULT_COMPANY_ID);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where companyName equals to
        defaultTimesheetReportFiltering("companyName.equals=" + DEFAULT_COMPANY_NAME, "companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where companyName in
        defaultTimesheetReportFiltering(
            "companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME,
            "companyName.in=" + UPDATED_COMPANY_NAME
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where companyName is not null
        defaultTimesheetReportFiltering("companyName.specified=true", "companyName.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCompanyNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where companyName contains
        defaultTimesheetReportFiltering("companyName.contains=" + DEFAULT_COMPANY_NAME, "companyName.contains=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCompanyNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where companyName does not contain
        defaultTimesheetReportFiltering(
            "companyName.doesNotContain=" + UPDATED_COMPANY_NAME,
            "companyName.doesNotContain=" + DEFAULT_COMPANY_NAME
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where startDate equals to
        defaultTimesheetReportFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where startDate in
        defaultTimesheetReportFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where startDate is not null
        defaultTimesheetReportFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where startDate is greater than or equal to
        defaultTimesheetReportFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where startDate is less than or equal to
        defaultTimesheetReportFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where startDate is less than
        defaultTimesheetReportFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where startDate is greater than
        defaultTimesheetReportFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where endDate equals to
        defaultTimesheetReportFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where endDate in
        defaultTimesheetReportFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where endDate is not null
        defaultTimesheetReportFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where endDate is greater than or equal to
        defaultTimesheetReportFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where endDate is less than or equal to
        defaultTimesheetReportFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where endDate is less than
        defaultTimesheetReportFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where endDate is greater than
        defaultTimesheetReportFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalRegularHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalRegularHours equals to
        defaultTimesheetReportFiltering(
            "totalRegularHours.equals=" + DEFAULT_TOTAL_REGULAR_HOURS,
            "totalRegularHours.equals=" + UPDATED_TOTAL_REGULAR_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalRegularHoursIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalRegularHours in
        defaultTimesheetReportFiltering(
            "totalRegularHours.in=" + DEFAULT_TOTAL_REGULAR_HOURS + "," + UPDATED_TOTAL_REGULAR_HOURS,
            "totalRegularHours.in=" + UPDATED_TOTAL_REGULAR_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalRegularHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalRegularHours is not null
        defaultTimesheetReportFiltering("totalRegularHours.specified=true", "totalRegularHours.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalRegularHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalRegularHours is greater than or equal to
        defaultTimesheetReportFiltering(
            "totalRegularHours.greaterThanOrEqual=" + DEFAULT_TOTAL_REGULAR_HOURS,
            "totalRegularHours.greaterThanOrEqual=" + UPDATED_TOTAL_REGULAR_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalRegularHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalRegularHours is less than or equal to
        defaultTimesheetReportFiltering(
            "totalRegularHours.lessThanOrEqual=" + DEFAULT_TOTAL_REGULAR_HOURS,
            "totalRegularHours.lessThanOrEqual=" + SMALLER_TOTAL_REGULAR_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalRegularHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalRegularHours is less than
        defaultTimesheetReportFiltering(
            "totalRegularHours.lessThan=" + UPDATED_TOTAL_REGULAR_HOURS,
            "totalRegularHours.lessThan=" + DEFAULT_TOTAL_REGULAR_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalRegularHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalRegularHours is greater than
        defaultTimesheetReportFiltering(
            "totalRegularHours.greaterThan=" + SMALLER_TOTAL_REGULAR_HOURS,
            "totalRegularHours.greaterThan=" + DEFAULT_TOTAL_REGULAR_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalOvertimeHoursIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalOvertimeHours equals to
        defaultTimesheetReportFiltering(
            "totalOvertimeHours.equals=" + DEFAULT_TOTAL_OVERTIME_HOURS,
            "totalOvertimeHours.equals=" + UPDATED_TOTAL_OVERTIME_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalOvertimeHoursIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalOvertimeHours in
        defaultTimesheetReportFiltering(
            "totalOvertimeHours.in=" + DEFAULT_TOTAL_OVERTIME_HOURS + "," + UPDATED_TOTAL_OVERTIME_HOURS,
            "totalOvertimeHours.in=" + UPDATED_TOTAL_OVERTIME_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalOvertimeHoursIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalOvertimeHours is not null
        defaultTimesheetReportFiltering("totalOvertimeHours.specified=true", "totalOvertimeHours.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalOvertimeHoursIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalOvertimeHours is greater than or equal to
        defaultTimesheetReportFiltering(
            "totalOvertimeHours.greaterThanOrEqual=" + DEFAULT_TOTAL_OVERTIME_HOURS,
            "totalOvertimeHours.greaterThanOrEqual=" + UPDATED_TOTAL_OVERTIME_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalOvertimeHoursIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalOvertimeHours is less than or equal to
        defaultTimesheetReportFiltering(
            "totalOvertimeHours.lessThanOrEqual=" + DEFAULT_TOTAL_OVERTIME_HOURS,
            "totalOvertimeHours.lessThanOrEqual=" + SMALLER_TOTAL_OVERTIME_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalOvertimeHoursIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalOvertimeHours is less than
        defaultTimesheetReportFiltering(
            "totalOvertimeHours.lessThan=" + UPDATED_TOTAL_OVERTIME_HOURS,
            "totalOvertimeHours.lessThan=" + DEFAULT_TOTAL_OVERTIME_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalOvertimeHoursIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalOvertimeHours is greater than
        defaultTimesheetReportFiltering(
            "totalOvertimeHours.greaterThan=" + SMALLER_TOTAL_OVERTIME_HOURS,
            "totalOvertimeHours.greaterThan=" + DEFAULT_TOTAL_OVERTIME_HOURS
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalAllowanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalAllowance equals to
        defaultTimesheetReportFiltering(
            "totalAllowance.equals=" + DEFAULT_TOTAL_ALLOWANCE,
            "totalAllowance.equals=" + UPDATED_TOTAL_ALLOWANCE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalAllowanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalAllowance in
        defaultTimesheetReportFiltering(
            "totalAllowance.in=" + DEFAULT_TOTAL_ALLOWANCE + "," + UPDATED_TOTAL_ALLOWANCE,
            "totalAllowance.in=" + UPDATED_TOTAL_ALLOWANCE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalAllowanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalAllowance is not null
        defaultTimesheetReportFiltering("totalAllowance.specified=true", "totalAllowance.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalAllowanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalAllowance is greater than or equal to
        defaultTimesheetReportFiltering(
            "totalAllowance.greaterThanOrEqual=" + DEFAULT_TOTAL_ALLOWANCE,
            "totalAllowance.greaterThanOrEqual=" + UPDATED_TOTAL_ALLOWANCE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalAllowanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalAllowance is less than or equal to
        defaultTimesheetReportFiltering(
            "totalAllowance.lessThanOrEqual=" + DEFAULT_TOTAL_ALLOWANCE,
            "totalAllowance.lessThanOrEqual=" + SMALLER_TOTAL_ALLOWANCE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalAllowanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalAllowance is less than
        defaultTimesheetReportFiltering(
            "totalAllowance.lessThan=" + UPDATED_TOTAL_ALLOWANCE,
            "totalAllowance.lessThan=" + DEFAULT_TOTAL_ALLOWANCE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByTotalAllowanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where totalAllowance is greater than
        defaultTimesheetReportFiltering(
            "totalAllowance.greaterThan=" + SMALLER_TOTAL_ALLOWANCE,
            "totalAllowance.greaterThan=" + DEFAULT_TOTAL_ALLOWANCE
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where status equals to
        defaultTimesheetReportFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where status in
        defaultTimesheetReportFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where status is not null
        defaultTimesheetReportFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where status contains
        defaultTimesheetReportFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where status does not contain
        defaultTimesheetReportFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByGeneratedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where generatedAt equals to
        defaultTimesheetReportFiltering("generatedAt.equals=" + DEFAULT_GENERATED_AT, "generatedAt.equals=" + UPDATED_GENERATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByGeneratedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where generatedAt in
        defaultTimesheetReportFiltering(
            "generatedAt.in=" + DEFAULT_GENERATED_AT + "," + UPDATED_GENERATED_AT,
            "generatedAt.in=" + UPDATED_GENERATED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByGeneratedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where generatedAt is not null
        defaultTimesheetReportFiltering("generatedAt.specified=true", "generatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByGeneratedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where generatedAt is greater than or equal to
        defaultTimesheetReportFiltering(
            "generatedAt.greaterThanOrEqual=" + DEFAULT_GENERATED_AT,
            "generatedAt.greaterThanOrEqual=" + UPDATED_GENERATED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByGeneratedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where generatedAt is less than or equal to
        defaultTimesheetReportFiltering(
            "generatedAt.lessThanOrEqual=" + DEFAULT_GENERATED_AT,
            "generatedAt.lessThanOrEqual=" + SMALLER_GENERATED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByGeneratedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where generatedAt is less than
        defaultTimesheetReportFiltering("generatedAt.lessThan=" + UPDATED_GENERATED_AT, "generatedAt.lessThan=" + DEFAULT_GENERATED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByGeneratedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where generatedAt is greater than
        defaultTimesheetReportFiltering(
            "generatedAt.greaterThan=" + SMALLER_GENERATED_AT,
            "generatedAt.greaterThan=" + DEFAULT_GENERATED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByApprovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where approvedAt equals to
        defaultTimesheetReportFiltering("approvedAt.equals=" + DEFAULT_APPROVED_AT, "approvedAt.equals=" + UPDATED_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByApprovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where approvedAt in
        defaultTimesheetReportFiltering(
            "approvedAt.in=" + DEFAULT_APPROVED_AT + "," + UPDATED_APPROVED_AT,
            "approvedAt.in=" + UPDATED_APPROVED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByApprovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where approvedAt is not null
        defaultTimesheetReportFiltering("approvedAt.specified=true", "approvedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByApprovedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where approvedAt is greater than or equal to
        defaultTimesheetReportFiltering(
            "approvedAt.greaterThanOrEqual=" + DEFAULT_APPROVED_AT,
            "approvedAt.greaterThanOrEqual=" + UPDATED_APPROVED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByApprovedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where approvedAt is less than or equal to
        defaultTimesheetReportFiltering(
            "approvedAt.lessThanOrEqual=" + DEFAULT_APPROVED_AT,
            "approvedAt.lessThanOrEqual=" + SMALLER_APPROVED_AT
        );
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByApprovedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where approvedAt is less than
        defaultTimesheetReportFiltering("approvedAt.lessThan=" + UPDATED_APPROVED_AT, "approvedAt.lessThan=" + DEFAULT_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByApprovedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where approvedAt is greater than
        defaultTimesheetReportFiltering("approvedAt.greaterThan=" + SMALLER_APPROVED_AT, "approvedAt.greaterThan=" + DEFAULT_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where comments equals to
        defaultTimesheetReportFiltering("comments.equals=" + DEFAULT_COMMENTS, "comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where comments in
        defaultTimesheetReportFiltering("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS, "comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where comments is not null
        defaultTimesheetReportFiltering("comments.specified=true", "comments.specified=false");
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCommentsContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where comments contains
        defaultTimesheetReportFiltering("comments.contains=" + DEFAULT_COMMENTS, "comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        // Get all the timesheetReportList where comments does not contain
        defaultTimesheetReportFiltering("comments.doesNotContain=" + UPDATED_COMMENTS, "comments.doesNotContain=" + DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByGeneratedByIsEqualToSomething() throws Exception {
        User generatedBy;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            timesheetReportRepository.saveAndFlush(timesheetReport);
            generatedBy = UserResourceIT.createEntity(em);
        } else {
            generatedBy = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(generatedBy);
        em.flush();
        timesheetReport.setGeneratedBy(generatedBy);
        timesheetReportRepository.saveAndFlush(timesheetReport);
        Long generatedById = generatedBy.getId();
        // Get all the timesheetReportList where generatedBy equals to generatedById
        defaultTimesheetReportShouldBeFound("generatedById.equals=" + generatedById);

        // Get all the timesheetReportList where generatedBy equals to (generatedById + 1)
        defaultTimesheetReportShouldNotBeFound("generatedById.equals=" + (generatedById + 1));
    }

    @Test
    @Transactional
    void getAllTimesheetReportsByApprovedByIsEqualToSomething() throws Exception {
        User approvedBy;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            timesheetReportRepository.saveAndFlush(timesheetReport);
            approvedBy = UserResourceIT.createEntity(em);
        } else {
            approvedBy = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(approvedBy);
        em.flush();
        timesheetReport.setApprovedBy(approvedBy);
        timesheetReportRepository.saveAndFlush(timesheetReport);
        Long approvedById = approvedBy.getId();
        // Get all the timesheetReportList where approvedBy equals to approvedById
        defaultTimesheetReportShouldBeFound("approvedById.equals=" + approvedById);

        // Get all the timesheetReportList where approvedBy equals to (approvedById + 1)
        defaultTimesheetReportShouldNotBeFound("approvedById.equals=" + (approvedById + 1));
    }

    private void defaultTimesheetReportFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTimesheetReportShouldBeFound(shouldBeFound);
        defaultTimesheetReportShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTimesheetReportShouldBeFound(String filter) throws Exception {
        restTimesheetReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timesheetReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalRegularHours").value(hasItem(DEFAULT_TOTAL_REGULAR_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].totalOvertimeHours").value(hasItem(DEFAULT_TOTAL_OVERTIME_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].totalAllowance").value(hasItem(DEFAULT_TOTAL_ALLOWANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].generatedAt").value(hasItem(sameInstant(DEFAULT_GENERATED_AT))))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(sameInstant(DEFAULT_APPROVED_AT))))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));

        // Check, that the count call also returns 1
        restTimesheetReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTimesheetReportShouldNotBeFound(String filter) throws Exception {
        restTimesheetReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTimesheetReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTimesheetReport() throws Exception {
        // Get the timesheetReport
        restTimesheetReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimesheetReport() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timesheetReport
        TimesheetReport updatedTimesheetReport = timesheetReportRepository.findById(timesheetReport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTimesheetReport are not directly saved in db
        em.detach(updatedTimesheetReport);
        updatedTimesheetReport
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME)
            .companyId(UPDATED_COMPANY_ID)
            .companyName(UPDATED_COMPANY_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .totalRegularHours(UPDATED_TOTAL_REGULAR_HOURS)
            .totalOvertimeHours(UPDATED_TOTAL_OVERTIME_HOURS)
            .totalAllowance(UPDATED_TOTAL_ALLOWANCE)
            .status(UPDATED_STATUS)
            .generatedAt(UPDATED_GENERATED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .comments(UPDATED_COMMENTS);
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(updatedTimesheetReport);

        restTimesheetReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timesheetReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timesheetReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the TimesheetReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTimesheetReportToMatchAllProperties(updatedTimesheetReport);
    }

    @Test
    @Transactional
    void putNonExistingTimesheetReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetReport.setId(longCount.incrementAndGet());

        // Create the TimesheetReport
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimesheetReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timesheetReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timesheetReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimesheetReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimesheetReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetReport.setId(longCount.incrementAndGet());

        // Create the TimesheetReport
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timesheetReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimesheetReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimesheetReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetReport.setId(longCount.incrementAndGet());

        // Create the TimesheetReport
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimesheetReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimesheetReportWithPatch() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timesheetReport using partial update
        TimesheetReport partialUpdatedTimesheetReport = new TimesheetReport();
        partialUpdatedTimesheetReport.setId(timesheetReport.getId());

        partialUpdatedTimesheetReport
            .userName(UPDATED_USER_NAME)
            .companyId(UPDATED_COMPANY_ID)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .generatedAt(UPDATED_GENERATED_AT)
            .approvedAt(UPDATED_APPROVED_AT);

        restTimesheetReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimesheetReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimesheetReport))
            )
            .andExpect(status().isOk());

        // Validate the TimesheetReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimesheetReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTimesheetReport, timesheetReport),
            getPersistedTimesheetReport(timesheetReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateTimesheetReportWithPatch() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timesheetReport using partial update
        TimesheetReport partialUpdatedTimesheetReport = new TimesheetReport();
        partialUpdatedTimesheetReport.setId(timesheetReport.getId());

        partialUpdatedTimesheetReport
            .userId(UPDATED_USER_ID)
            .userName(UPDATED_USER_NAME)
            .companyId(UPDATED_COMPANY_ID)
            .companyName(UPDATED_COMPANY_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .totalRegularHours(UPDATED_TOTAL_REGULAR_HOURS)
            .totalOvertimeHours(UPDATED_TOTAL_OVERTIME_HOURS)
            .totalAllowance(UPDATED_TOTAL_ALLOWANCE)
            .status(UPDATED_STATUS)
            .generatedAt(UPDATED_GENERATED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .comments(UPDATED_COMMENTS);

        restTimesheetReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimesheetReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimesheetReport))
            )
            .andExpect(status().isOk());

        // Validate the TimesheetReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimesheetReportUpdatableFieldsEquals(
            partialUpdatedTimesheetReport,
            getPersistedTimesheetReport(partialUpdatedTimesheetReport)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTimesheetReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetReport.setId(longCount.incrementAndGet());

        // Create the TimesheetReport
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimesheetReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timesheetReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timesheetReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimesheetReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimesheetReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetReport.setId(longCount.incrementAndGet());

        // Create the TimesheetReport
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timesheetReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimesheetReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimesheetReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timesheetReport.setId(longCount.incrementAndGet());

        // Create the TimesheetReport
        TimesheetReportDTO timesheetReportDTO = timesheetReportMapper.toDto(timesheetReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimesheetReportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(timesheetReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimesheetReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimesheetReport() throws Exception {
        // Initialize the database
        insertedTimesheetReport = timesheetReportRepository.saveAndFlush(timesheetReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the timesheetReport
        restTimesheetReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, timesheetReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return timesheetReportRepository.count();
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

    protected TimesheetReport getPersistedTimesheetReport(TimesheetReport timesheetReport) {
        return timesheetReportRepository.findById(timesheetReport.getId()).orElseThrow();
    }

    protected void assertPersistedTimesheetReportToMatchAllProperties(TimesheetReport expectedTimesheetReport) {
        assertTimesheetReportAllPropertiesEquals(expectedTimesheetReport, getPersistedTimesheetReport(expectedTimesheetReport));
    }

    protected void assertPersistedTimesheetReportToMatchUpdatableProperties(TimesheetReport expectedTimesheetReport) {
        assertTimesheetReportAllUpdatablePropertiesEquals(expectedTimesheetReport, getPersistedTimesheetReport(expectedTimesheetReport));
    }
}
