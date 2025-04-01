package com.timesheetsystem.service;

import com.timesheetsystem.service.dto.TimesheetAuditDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.timesheetsystem.domain.TimesheetAudit}.
 */
public interface TimesheetAuditService {
    /**
     * Save a timesheetAudit.
     *
     * @param timesheetAuditDTO the entity to save.
     * @return the persisted entity.
     */
    TimesheetAuditDTO save(TimesheetAuditDTO timesheetAuditDTO);

    /**
     * Updates a timesheetAudit.
     *
     * @param timesheetAuditDTO the entity to update.
     * @return the persisted entity.
     */
    TimesheetAuditDTO update(TimesheetAuditDTO timesheetAuditDTO);

    /**
     * Partially updates a timesheetAudit.
     *
     * @param timesheetAuditDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TimesheetAuditDTO> partialUpdate(TimesheetAuditDTO timesheetAuditDTO);

    /**
     * Get all the timesheetAudits.
     *
     * @return the list of entities.
     */
    List<TimesheetAuditDTO> findAll();

    /**
     * Get the "id" timesheetAudit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TimesheetAuditDTO> findOne(Long id);

    /**
     * Delete the "id" timesheetAudit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
