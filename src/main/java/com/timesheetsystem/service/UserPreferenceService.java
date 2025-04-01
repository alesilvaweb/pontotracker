package com.timesheetsystem.service;

import com.timesheetsystem.service.dto.UserPreferenceDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.timesheetsystem.domain.UserPreference}.
 */
public interface UserPreferenceService {
    /**
     * Save a userPreference.
     *
     * @param userPreferenceDTO the entity to save.
     * @return the persisted entity.
     */
    UserPreferenceDTO save(UserPreferenceDTO userPreferenceDTO);

    /**
     * Updates a userPreference.
     *
     * @param userPreferenceDTO the entity to update.
     * @return the persisted entity.
     */
    UserPreferenceDTO update(UserPreferenceDTO userPreferenceDTO);

    /**
     * Partially updates a userPreference.
     *
     * @param userPreferenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserPreferenceDTO> partialUpdate(UserPreferenceDTO userPreferenceDTO);

    /**
     * Get all the userPreferences.
     *
     * @return the list of entities.
     */
    List<UserPreferenceDTO> findAll();

    /**
     * Get all the userPreferences with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserPreferenceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userPreference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserPreferenceDTO> findOne(Long id);

    /**
     * Delete the "id" userPreference.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
