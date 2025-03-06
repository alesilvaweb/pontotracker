package com.timesheetsystem.web.rest;

import static com.timesheetsystem.domain.UserPreferenceAsserts.*;
import static com.timesheetsystem.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timesheetsystem.IntegrationTest;
import com.timesheetsystem.domain.User;
import com.timesheetsystem.domain.UserPreference;
import com.timesheetsystem.repository.UserPreferenceRepository;
import com.timesheetsystem.repository.UserRepository;
import com.timesheetsystem.service.UserPreferenceService;
import com.timesheetsystem.service.dto.UserPreferenceDTO;
import com.timesheetsystem.service.mapper.UserPreferenceMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link UserPreferenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserPreferenceResourceIT {

    private static final Long DEFAULT_DEFAULT_COMPANY_ID = 1L;
    private static final Long UPDATED_DEFAULT_COMPANY_ID = 2L;

    private static final Boolean DEFAULT_EMAIL_NOTIFICATIONS = false;
    private static final Boolean UPDATED_EMAIL_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_SMS_NOTIFICATIONS = false;
    private static final Boolean UPDATED_SMS_NOTIFICATIONS = true;

    private static final Boolean DEFAULT_PUSH_NOTIFICATIONS = false;
    private static final Boolean UPDATED_PUSH_NOTIFICATIONS = true;

    private static final String DEFAULT_REPORT_FREQUENCY = "AAAAAAAAAA";
    private static final String UPDATED_REPORT_FREQUENCY = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEEK_START_DAY = 0;
    private static final Integer UPDATED_WEEK_START_DAY = 1;

    private static final String ENTITY_API_URL = "/api/user-preferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserPreferenceRepository userPreferenceRepositoryMock;

    @Autowired
    private UserPreferenceMapper userPreferenceMapper;

    @Mock
    private UserPreferenceService userPreferenceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserPreferenceMockMvc;

    private UserPreference userPreference;

    private UserPreference insertedUserPreference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPreference createEntity(EntityManager em) {
        UserPreference userPreference = new UserPreference()
            .defaultCompanyId(DEFAULT_DEFAULT_COMPANY_ID)
            .emailNotifications(DEFAULT_EMAIL_NOTIFICATIONS)
            .smsNotifications(DEFAULT_SMS_NOTIFICATIONS)
            .pushNotifications(DEFAULT_PUSH_NOTIFICATIONS)
            .reportFrequency(DEFAULT_REPORT_FREQUENCY)
            .weekStartDay(DEFAULT_WEEK_START_DAY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userPreference.setUser(user);
        return userPreference;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserPreference createUpdatedEntity(EntityManager em) {
        UserPreference userPreference = new UserPreference()
            .defaultCompanyId(UPDATED_DEFAULT_COMPANY_ID)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .smsNotifications(UPDATED_SMS_NOTIFICATIONS)
            .pushNotifications(UPDATED_PUSH_NOTIFICATIONS)
            .reportFrequency(UPDATED_REPORT_FREQUENCY)
            .weekStartDay(UPDATED_WEEK_START_DAY);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userPreference.setUser(user);
        return userPreference;
    }

    @BeforeEach
    public void initTest() {
        userPreference = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserPreference != null) {
            userPreferenceRepository.delete(insertedUserPreference);
            insertedUserPreference = null;
        }
    }

    @Test
    @Transactional
    void createUserPreference() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserPreference
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(userPreference);
        var returnedUserPreferenceDTO = om.readValue(
            restUserPreferenceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPreferenceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserPreferenceDTO.class
        );

        // Validate the UserPreference in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserPreference = userPreferenceMapper.toEntity(returnedUserPreferenceDTO);
        assertUserPreferenceUpdatableFieldsEquals(returnedUserPreference, getPersistedUserPreference(returnedUserPreference));

        insertedUserPreference = returnedUserPreference;
    }

    @Test
    @Transactional
    void createUserPreferenceWithExistingId() throws Exception {
        // Create the UserPreference with an existing ID
        userPreference.setId(1L);
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(userPreference);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPreferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDefaultCompanyIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userPreference.setDefaultCompanyId(null);

        // Create the UserPreference, which fails.
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(userPreference);

        restUserPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPreferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailNotificationsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userPreference.setEmailNotifications(null);

        // Create the UserPreference, which fails.
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(userPreference);

        restUserPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPreferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSmsNotificationsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userPreference.setSmsNotifications(null);

        // Create the UserPreference, which fails.
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(userPreference);

        restUserPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPreferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPushNotificationsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userPreference.setPushNotifications(null);

        // Create the UserPreference, which fails.
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(userPreference);

        restUserPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPreferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserPreferences() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get all the userPreferenceList
        restUserPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userPreference.getId().intValue())))
            .andExpect(jsonPath("$.[*].defaultCompanyId").value(hasItem(DEFAULT_DEFAULT_COMPANY_ID.intValue())))
            .andExpect(jsonPath("$.[*].emailNotifications").value(hasItem(DEFAULT_EMAIL_NOTIFICATIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].smsNotifications").value(hasItem(DEFAULT_SMS_NOTIFICATIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].pushNotifications").value(hasItem(DEFAULT_PUSH_NOTIFICATIONS.booleanValue())))
            .andExpect(jsonPath("$.[*].reportFrequency").value(hasItem(DEFAULT_REPORT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].weekStartDay").value(hasItem(DEFAULT_WEEK_START_DAY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserPreferencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(userPreferenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserPreferenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userPreferenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserPreferencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userPreferenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserPreferenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userPreferenceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserPreference() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        // Get the userPreference
        restUserPreferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, userPreference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userPreference.getId().intValue()))
            .andExpect(jsonPath("$.defaultCompanyId").value(DEFAULT_DEFAULT_COMPANY_ID.intValue()))
            .andExpect(jsonPath("$.emailNotifications").value(DEFAULT_EMAIL_NOTIFICATIONS.booleanValue()))
            .andExpect(jsonPath("$.smsNotifications").value(DEFAULT_SMS_NOTIFICATIONS.booleanValue()))
            .andExpect(jsonPath("$.pushNotifications").value(DEFAULT_PUSH_NOTIFICATIONS.booleanValue()))
            .andExpect(jsonPath("$.reportFrequency").value(DEFAULT_REPORT_FREQUENCY))
            .andExpect(jsonPath("$.weekStartDay").value(DEFAULT_WEEK_START_DAY));
    }

    @Test
    @Transactional
    void getNonExistingUserPreference() throws Exception {
        // Get the userPreference
        restUserPreferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserPreference() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userPreference
        UserPreference updatedUserPreference = userPreferenceRepository.findById(userPreference.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserPreference are not directly saved in db
        em.detach(updatedUserPreference);
        updatedUserPreference
            .defaultCompanyId(UPDATED_DEFAULT_COMPANY_ID)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .smsNotifications(UPDATED_SMS_NOTIFICATIONS)
            .pushNotifications(UPDATED_PUSH_NOTIFICATIONS)
            .reportFrequency(UPDATED_REPORT_FREQUENCY)
            .weekStartDay(UPDATED_WEEK_START_DAY);
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(updatedUserPreference);

        restUserPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userPreferenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userPreferenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserPreferenceToMatchAllProperties(updatedUserPreference);
    }

    @Test
    @Transactional
    void putNonExistingUserPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreference.setId(longCount.incrementAndGet());

        // Create the UserPreference
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(userPreference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userPreferenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreference.setId(longCount.incrementAndGet());

        // Create the UserPreference
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(userPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreference.setId(longCount.incrementAndGet());

        // Create the UserPreference
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(userPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userPreferenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserPreferenceWithPatch() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userPreference using partial update
        UserPreference partialUpdatedUserPreference = new UserPreference();
        partialUpdatedUserPreference.setId(userPreference.getId());

        partialUpdatedUserPreference
            .defaultCompanyId(UPDATED_DEFAULT_COMPANY_ID)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .smsNotifications(UPDATED_SMS_NOTIFICATIONS)
            .reportFrequency(UPDATED_REPORT_FREQUENCY)
            .weekStartDay(UPDATED_WEEK_START_DAY);

        restUserPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserPreference))
            )
            .andExpect(status().isOk());

        // Validate the UserPreference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserPreferenceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserPreference, userPreference),
            getPersistedUserPreference(userPreference)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserPreferenceWithPatch() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userPreference using partial update
        UserPreference partialUpdatedUserPreference = new UserPreference();
        partialUpdatedUserPreference.setId(userPreference.getId());

        partialUpdatedUserPreference
            .defaultCompanyId(UPDATED_DEFAULT_COMPANY_ID)
            .emailNotifications(UPDATED_EMAIL_NOTIFICATIONS)
            .smsNotifications(UPDATED_SMS_NOTIFICATIONS)
            .pushNotifications(UPDATED_PUSH_NOTIFICATIONS)
            .reportFrequency(UPDATED_REPORT_FREQUENCY)
            .weekStartDay(UPDATED_WEEK_START_DAY);

        restUserPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserPreference))
            )
            .andExpect(status().isOk());

        // Validate the UserPreference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserPreferenceUpdatableFieldsEquals(partialUpdatedUserPreference, getPersistedUserPreference(partialUpdatedUserPreference));
    }

    @Test
    @Transactional
    void patchNonExistingUserPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreference.setId(longCount.incrementAndGet());

        // Create the UserPreference
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(userPreference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userPreferenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreference.setId(longCount.incrementAndGet());

        // Create the UserPreference
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(userPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userPreference.setId(longCount.incrementAndGet());

        // Create the UserPreference
        UserPreferenceDTO userPreferenceDTO = userPreferenceMapper.toDto(userPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserPreferenceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userPreferenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserPreference() throws Exception {
        // Initialize the database
        insertedUserPreference = userPreferenceRepository.saveAndFlush(userPreference);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userPreference
        restUserPreferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, userPreference.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userPreferenceRepository.count();
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

    protected UserPreference getPersistedUserPreference(UserPreference userPreference) {
        return userPreferenceRepository.findById(userPreference.getId()).orElseThrow();
    }

    protected void assertPersistedUserPreferenceToMatchAllProperties(UserPreference expectedUserPreference) {
        assertUserPreferenceAllPropertiesEquals(expectedUserPreference, getPersistedUserPreference(expectedUserPreference));
    }

    protected void assertPersistedUserPreferenceToMatchUpdatableProperties(UserPreference expectedUserPreference) {
        assertUserPreferenceAllUpdatablePropertiesEquals(expectedUserPreference, getPersistedUserPreference(expectedUserPreference));
    }
}
