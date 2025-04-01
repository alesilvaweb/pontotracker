package com.timesheetsystem.service;

import com.timesheetsystem.service.dto.TimeEntryDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.timesheetsystem.domain.TimeEntry}.
 */
public interface TimeEntryService {
    /**
     * Save a timeEntry.
     *
     * @param timeEntryDTO the entity to save.
     * @return the persisted entity.
     */
    TimeEntryDTO save(TimeEntryDTO timeEntryDTO);

    /**
     * Updates a timeEntry.
     *
     * @param timeEntryDTO the entity to update.
     * @return the persisted entity.
     */
    TimeEntryDTO update(TimeEntryDTO timeEntryDTO);

    /**
     * Partially updates a timeEntry.
     *
     * @param timeEntryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TimeEntryDTO> partialUpdate(TimeEntryDTO timeEntryDTO);

    /**
     * Get the "id" timeEntry.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TimeEntryDTO> findOne(Long id);

    /**
     * Delete the "id" timeEntry.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
