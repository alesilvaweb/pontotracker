package com.timesheetsystem.service;

import com.timesheetsystem.service.dto.TimesheetAlertDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.timesheetsystem.domain.TimesheetAlert}.
 */
public interface TimesheetAlertService {
    /**
     * Save a timesheetAlert.
     *
     * @param timesheetAlertDTO the entity to save.
     * @return the persisted entity.
     */
    TimesheetAlertDTO save(TimesheetAlertDTO timesheetAlertDTO);

    /**
     * Updates a timesheetAlert.
     *
     * @param timesheetAlertDTO the entity to update.
     * @return the persisted entity.
     */
    TimesheetAlertDTO update(TimesheetAlertDTO timesheetAlertDTO);

    /**
     * Partially updates a timesheetAlert.
     *
     * @param timesheetAlertDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TimesheetAlertDTO> partialUpdate(TimesheetAlertDTO timesheetAlertDTO);

    /**
     * Get all the timesheetAlerts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TimesheetAlertDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" timesheetAlert.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TimesheetAlertDTO> findOne(Long id);

    /**
     * Delete the "id" timesheetAlert.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
