package com.timesheetsystem.service;

import com.timesheetsystem.service.dto.TimesheetDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.timesheetsystem.domain.Timesheet}.
 */
public interface TimesheetService {
    /**
     * Save a timesheet.
     *
     * @param timesheetDTO the entity to save.
     * @return the persisted entity.
     */
    TimesheetDTO save(TimesheetDTO timesheetDTO);

    /**
     * Updates a timesheet.
     *
     * @param timesheetDTO the entity to update.
     * @return the persisted entity.
     */
    TimesheetDTO update(TimesheetDTO timesheetDTO);

    /**
     * Partially updates a timesheet.
     *
     * @param timesheetDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TimesheetDTO> partialUpdate(TimesheetDTO timesheetDTO);

    /**
     * Get all the timesheets with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TimesheetDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" timesheet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TimesheetDTO> findOne(Long id);

    /**
     * Delete the "id" timesheet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
