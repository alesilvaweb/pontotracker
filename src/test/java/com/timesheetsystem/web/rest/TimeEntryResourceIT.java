package com.timesheetsystem.web.rest;

import static com.timesheetsystem.domain.TimeEntryAsserts.*;
import static com.timesheetsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static com.timesheetsystem.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timesheetsystem.IntegrationTest;
import com.timesheetsystem.domain.TimeEntry;
import com.timesheetsystem.domain.Timesheet;
import com.timesheetsystem.domain.enumeration.EntryType;
import com.timesheetsystem.domain.enumeration.OvertimeCategory;
import com.timesheetsystem.repository.TimeEntryRepository;
import com.timesheetsystem.service.dto.TimeEntryDTO;
import com.timesheetsystem.service.mapper.TimeEntryMapper;
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
 * Integration tests for the {@link TimeEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimeEntryResourceIT {

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final EntryType DEFAULT_TYPE = EntryType.REGULAR;
    private static final EntryType UPDATED_TYPE = EntryType.OVERTIME;

    private static final OvertimeCategory DEFAULT_OVERTIME_CATEGORY = OvertimeCategory.NORMAL;
    private static final OvertimeCategory UPDATED_OVERTIME_CATEGORY = OvertimeCategory.SPECIAL;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_HOURS_WORKED = 0D;
    private static final Double UPDATED_HOURS_WORKED = 1D;
    private static final Double SMALLER_HOURS_WORKED = 0D - 1D;

    private static final String ENTITY_API_URL = "/api/time-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TimeEntryRepository timeEntryRepository;

    @Autowired
    private TimeEntryMapper timeEntryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimeEntryMockMvc;

    private TimeEntry timeEntry;

    private TimeEntry insertedTimeEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeEntry createEntity(EntityManager em) {
        TimeEntry timeEntry = new TimeEntry()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .type(DEFAULT_TYPE)
            .overtimeCategory(DEFAULT_OVERTIME_CATEGORY)
            .description(DEFAULT_DESCRIPTION)
            .hoursWorked(DEFAULT_HOURS_WORKED);
        // Add required entity
        Timesheet timesheet;
        if (TestUtil.findAll(em, Timesheet.class).isEmpty()) {
            timesheet = TimesheetResourceIT.createEntity(em);
            em.persist(timesheet);
            em.flush();
        } else {
            timesheet = TestUtil.findAll(em, Timesheet.class).get(0);
        }
        timeEntry.setTimesheet(timesheet);
        return timeEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeEntry createUpdatedEntity(EntityManager em) {
        TimeEntry timeEntry = new TimeEntry()
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .type(UPDATED_TYPE)
            .overtimeCategory(UPDATED_OVERTIME_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .hoursWorked(UPDATED_HOURS_WORKED);
        // Add required entity
        Timesheet timesheet;
        if (TestUtil.findAll(em, Timesheet.class).isEmpty()) {
            timesheet = TimesheetResourceIT.createUpdatedEntity(em);
            em.persist(timesheet);
            em.flush();
        } else {
            timesheet = TestUtil.findAll(em, Timesheet.class).get(0);
        }
        timeEntry.setTimesheet(timesheet);
        return timeEntry;
    }

    @BeforeEach
    public void initTest() {
        timeEntry = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTimeEntry != null) {
            timeEntryRepository.delete(insertedTimeEntry);
            insertedTimeEntry = null;
        }
    }

    @Test
    @Transactional
    void createTimeEntry() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TimeEntry
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(timeEntry);
        var returnedTimeEntryDTO = om.readValue(
            restTimeEntryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeEntryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TimeEntryDTO.class
        );

        // Validate the TimeEntry in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTimeEntry = timeEntryMapper.toEntity(returnedTimeEntryDTO);
        assertTimeEntryUpdatableFieldsEquals(returnedTimeEntry, getPersistedTimeEntry(returnedTimeEntry));

        insertedTimeEntry = returnedTimeEntry;
    }

    @Test
    @Transactional
    void createTimeEntryWithExistingId() throws Exception {
        // Create the TimeEntry with an existing ID
        timeEntry.setId(1L);
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(timeEntry);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TimeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timeEntry.setStartTime(null);

        // Create the TimeEntry, which fails.
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(timeEntry);

        restTimeEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timeEntry.setEndTime(null);

        // Create the TimeEntry, which fails.
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(timeEntry);

        restTimeEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timeEntry.setType(null);

        // Create the TimeEntry, which fails.
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(timeEntry);

        restTimeEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHoursWorkedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        timeEntry.setHoursWorked(null);

        // Create the TimeEntry, which fails.
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(timeEntry);

        restTimeEntryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeEntryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTimeEntries() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList
        restTimeEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].overtimeCategory").value(hasItem(DEFAULT_OVERTIME_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].hoursWorked").value(hasItem(DEFAULT_HOURS_WORKED.doubleValue())));
    }

    @Test
    @Transactional
    void getTimeEntry() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get the timeEntry
        restTimeEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, timeEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timeEntry.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.overtimeCategory").value(DEFAULT_OVERTIME_CATEGORY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.hoursWorked").value(DEFAULT_HOURS_WORKED.doubleValue()));
    }

    @Test
    @Transactional
    void getTimeEntriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        Long id = timeEntry.getId();

        defaultTimeEntryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTimeEntryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTimeEntryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where startTime equals to
        defaultTimeEntryFiltering("startTime.equals=" + DEFAULT_START_TIME, "startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where startTime in
        defaultTimeEntryFiltering("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME, "startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where startTime is not null
        defaultTimeEntryFiltering("startTime.specified=true", "startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTimeEntriesByStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where startTime is greater than or equal to
        defaultTimeEntryFiltering(
            "startTime.greaterThanOrEqual=" + DEFAULT_START_TIME,
            "startTime.greaterThanOrEqual=" + UPDATED_START_TIME
        );
    }

    @Test
    @Transactional
    void getAllTimeEntriesByStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where startTime is less than or equal to
        defaultTimeEntryFiltering("startTime.lessThanOrEqual=" + DEFAULT_START_TIME, "startTime.lessThanOrEqual=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where startTime is less than
        defaultTimeEntryFiltering("startTime.lessThan=" + UPDATED_START_TIME, "startTime.lessThan=" + DEFAULT_START_TIME);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where startTime is greater than
        defaultTimeEntryFiltering("startTime.greaterThan=" + SMALLER_START_TIME, "startTime.greaterThan=" + DEFAULT_START_TIME);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where endTime equals to
        defaultTimeEntryFiltering("endTime.equals=" + DEFAULT_END_TIME, "endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where endTime in
        defaultTimeEntryFiltering("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME, "endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where endTime is not null
        defaultTimeEntryFiltering("endTime.specified=true", "endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllTimeEntriesByEndTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where endTime is greater than or equal to
        defaultTimeEntryFiltering("endTime.greaterThanOrEqual=" + DEFAULT_END_TIME, "endTime.greaterThanOrEqual=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByEndTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where endTime is less than or equal to
        defaultTimeEntryFiltering("endTime.lessThanOrEqual=" + DEFAULT_END_TIME, "endTime.lessThanOrEqual=" + SMALLER_END_TIME);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByEndTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where endTime is less than
        defaultTimeEntryFiltering("endTime.lessThan=" + UPDATED_END_TIME, "endTime.lessThan=" + DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByEndTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where endTime is greater than
        defaultTimeEntryFiltering("endTime.greaterThan=" + SMALLER_END_TIME, "endTime.greaterThan=" + DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where type equals to
        defaultTimeEntryFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where type in
        defaultTimeEntryFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where type is not null
        defaultTimeEntryFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllTimeEntriesByOvertimeCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where overtimeCategory equals to
        defaultTimeEntryFiltering(
            "overtimeCategory.equals=" + DEFAULT_OVERTIME_CATEGORY,
            "overtimeCategory.equals=" + UPDATED_OVERTIME_CATEGORY
        );
    }

    @Test
    @Transactional
    void getAllTimeEntriesByOvertimeCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where overtimeCategory in
        defaultTimeEntryFiltering(
            "overtimeCategory.in=" + DEFAULT_OVERTIME_CATEGORY + "," + UPDATED_OVERTIME_CATEGORY,
            "overtimeCategory.in=" + UPDATED_OVERTIME_CATEGORY
        );
    }

    @Test
    @Transactional
    void getAllTimeEntriesByOvertimeCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where overtimeCategory is not null
        defaultTimeEntryFiltering("overtimeCategory.specified=true", "overtimeCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllTimeEntriesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where description equals to
        defaultTimeEntryFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where description in
        defaultTimeEntryFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTimeEntriesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where description is not null
        defaultTimeEntryFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTimeEntriesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where description contains
        defaultTimeEntryFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where description does not contain
        defaultTimeEntryFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByHoursWorkedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where hoursWorked equals to
        defaultTimeEntryFiltering("hoursWorked.equals=" + DEFAULT_HOURS_WORKED, "hoursWorked.equals=" + UPDATED_HOURS_WORKED);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByHoursWorkedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where hoursWorked in
        defaultTimeEntryFiltering(
            "hoursWorked.in=" + DEFAULT_HOURS_WORKED + "," + UPDATED_HOURS_WORKED,
            "hoursWorked.in=" + UPDATED_HOURS_WORKED
        );
    }

    @Test
    @Transactional
    void getAllTimeEntriesByHoursWorkedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where hoursWorked is not null
        defaultTimeEntryFiltering("hoursWorked.specified=true", "hoursWorked.specified=false");
    }

    @Test
    @Transactional
    void getAllTimeEntriesByHoursWorkedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where hoursWorked is greater than or equal to
        defaultTimeEntryFiltering(
            "hoursWorked.greaterThanOrEqual=" + DEFAULT_HOURS_WORKED,
            "hoursWorked.greaterThanOrEqual=" + UPDATED_HOURS_WORKED
        );
    }

    @Test
    @Transactional
    void getAllTimeEntriesByHoursWorkedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where hoursWorked is less than or equal to
        defaultTimeEntryFiltering(
            "hoursWorked.lessThanOrEqual=" + DEFAULT_HOURS_WORKED,
            "hoursWorked.lessThanOrEqual=" + SMALLER_HOURS_WORKED
        );
    }

    @Test
    @Transactional
    void getAllTimeEntriesByHoursWorkedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where hoursWorked is less than
        defaultTimeEntryFiltering("hoursWorked.lessThan=" + UPDATED_HOURS_WORKED, "hoursWorked.lessThan=" + DEFAULT_HOURS_WORKED);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByHoursWorkedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        // Get all the timeEntryList where hoursWorked is greater than
        defaultTimeEntryFiltering("hoursWorked.greaterThan=" + SMALLER_HOURS_WORKED, "hoursWorked.greaterThan=" + DEFAULT_HOURS_WORKED);
    }

    @Test
    @Transactional
    void getAllTimeEntriesByTimesheetIsEqualToSomething() throws Exception {
        Timesheet timesheet;
        if (TestUtil.findAll(em, Timesheet.class).isEmpty()) {
            timeEntryRepository.saveAndFlush(timeEntry);
            timesheet = TimesheetResourceIT.createEntity(em);
        } else {
            timesheet = TestUtil.findAll(em, Timesheet.class).get(0);
        }
        em.persist(timesheet);
        em.flush();
        timeEntry.setTimesheet(timesheet);
        timeEntryRepository.saveAndFlush(timeEntry);
        Long timesheetId = timesheet.getId();
        // Get all the timeEntryList where timesheet equals to timesheetId
        defaultTimeEntryShouldBeFound("timesheetId.equals=" + timesheetId);

        // Get all the timeEntryList where timesheet equals to (timesheetId + 1)
        defaultTimeEntryShouldNotBeFound("timesheetId.equals=" + (timesheetId + 1));
    }

    private void defaultTimeEntryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTimeEntryShouldBeFound(shouldBeFound);
        defaultTimeEntryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTimeEntryShouldBeFound(String filter) throws Exception {
        restTimeEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].overtimeCategory").value(hasItem(DEFAULT_OVERTIME_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].hoursWorked").value(hasItem(DEFAULT_HOURS_WORKED.doubleValue())));

        // Check, that the count call also returns 1
        restTimeEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTimeEntryShouldNotBeFound(String filter) throws Exception {
        restTimeEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTimeEntryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTimeEntry() throws Exception {
        // Get the timeEntry
        restTimeEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTimeEntry() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeEntry
        TimeEntry updatedTimeEntry = timeEntryRepository.findById(timeEntry.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTimeEntry are not directly saved in db
        em.detach(updatedTimeEntry);
        updatedTimeEntry
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .type(UPDATED_TYPE)
            .overtimeCategory(UPDATED_OVERTIME_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .hoursWorked(UPDATED_HOURS_WORKED);
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(updatedTimeEntry);

        restTimeEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timeEntryDTO))
            )
            .andExpect(status().isOk());

        // Validate the TimeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTimeEntryToMatchAllProperties(updatedTimeEntry);
    }

    @Test
    @Transactional
    void putNonExistingTimeEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeEntry.setId(longCount.incrementAndGet());

        // Create the TimeEntry
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(timeEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeEntryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timeEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimeEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeEntry.setId(longCount.incrementAndGet());

        // Create the TimeEntry
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(timeEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(timeEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimeEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeEntry.setId(longCount.incrementAndGet());

        // Create the TimeEntry
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(timeEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeEntryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(timeEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimeEntryWithPatch() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeEntry using partial update
        TimeEntry partialUpdatedTimeEntry = new TimeEntry();
        partialUpdatedTimeEntry.setId(timeEntry.getId());

        partialUpdatedTimeEntry.startTime(UPDATED_START_TIME).type(UPDATED_TYPE).hoursWorked(UPDATED_HOURS_WORKED);

        restTimeEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimeEntry))
            )
            .andExpect(status().isOk());

        // Validate the TimeEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimeEntryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTimeEntry, timeEntry),
            getPersistedTimeEntry(timeEntry)
        );
    }

    @Test
    @Transactional
    void fullUpdateTimeEntryWithPatch() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the timeEntry using partial update
        TimeEntry partialUpdatedTimeEntry = new TimeEntry();
        partialUpdatedTimeEntry.setId(timeEntry.getId());

        partialUpdatedTimeEntry
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .type(UPDATED_TYPE)
            .overtimeCategory(UPDATED_OVERTIME_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .hoursWorked(UPDATED_HOURS_WORKED);

        restTimeEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTimeEntry))
            )
            .andExpect(status().isOk());

        // Validate the TimeEntry in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTimeEntryUpdatableFieldsEquals(partialUpdatedTimeEntry, getPersistedTimeEntry(partialUpdatedTimeEntry));
    }

    @Test
    @Transactional
    void patchNonExistingTimeEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeEntry.setId(longCount.incrementAndGet());

        // Create the TimeEntry
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(timeEntry);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timeEntryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timeEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimeEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeEntry.setId(longCount.incrementAndGet());

        // Create the TimeEntry
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(timeEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(timeEntryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimeEntry() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        timeEntry.setId(longCount.incrementAndGet());

        // Create the TimeEntry
        TimeEntryDTO timeEntryDTO = timeEntryMapper.toDto(timeEntry);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeEntryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(timeEntryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeEntry in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimeEntry() throws Exception {
        // Initialize the database
        insertedTimeEntry = timeEntryRepository.saveAndFlush(timeEntry);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the timeEntry
        restTimeEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, timeEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return timeEntryRepository.count();
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

    protected TimeEntry getPersistedTimeEntry(TimeEntry timeEntry) {
        return timeEntryRepository.findById(timeEntry.getId()).orElseThrow();
    }

    protected void assertPersistedTimeEntryToMatchAllProperties(TimeEntry expectedTimeEntry) {
        assertTimeEntryAllPropertiesEquals(expectedTimeEntry, getPersistedTimeEntry(expectedTimeEntry));
    }

    protected void assertPersistedTimeEntryToMatchUpdatableProperties(TimeEntry expectedTimeEntry) {
        assertTimeEntryAllUpdatablePropertiesEquals(expectedTimeEntry, getPersistedTimeEntry(expectedTimeEntry));
    }
}
