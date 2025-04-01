package com.timesheetsystem.service;

import com.timesheetsystem.service.dto.TimesheetReportDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.timesheetsystem.domain.TimesheetReport}.
 */
public interface TimesheetReportService {
    /**
     * Save a timesheetReport.
     *
     * @param timesheetReportDTO the entity to save.
     * @return the persisted entity.
     */
    TimesheetReportDTO save(TimesheetReportDTO timesheetReportDTO);

    /**
     * Updates a timesheetReport.
     *
     * @param timesheetReportDTO the entity to update.
     * @return the persisted entity.
     */
    TimesheetReportDTO update(TimesheetReportDTO timesheetReportDTO);

    /**
     * Partially updates a timesheetReport.
     *
     * @param timesheetReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TimesheetReportDTO> partialUpdate(TimesheetReportDTO timesheetReportDTO);

    /**
     * Get all the timesheetReports with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TimesheetReportDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" timesheetReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TimesheetReportDTO> findOne(Long id);

    /**
     * Delete the "id" timesheetReport.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
