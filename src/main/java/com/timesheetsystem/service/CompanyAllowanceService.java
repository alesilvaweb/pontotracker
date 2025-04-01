package com.timesheetsystem.service;

import com.timesheetsystem.service.dto.CompanyAllowanceDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.timesheetsystem.domain.CompanyAllowance}.
 */
public interface CompanyAllowanceService {
    /**
     * Save a companyAllowance.
     *
     * @param companyAllowanceDTO the entity to save.
     * @return the persisted entity.
     */
    CompanyAllowanceDTO save(CompanyAllowanceDTO companyAllowanceDTO);

    /**
     * Updates a companyAllowance.
     *
     * @param companyAllowanceDTO the entity to update.
     * @return the persisted entity.
     */
    CompanyAllowanceDTO update(CompanyAllowanceDTO companyAllowanceDTO);

    /**
     * Partially updates a companyAllowance.
     *
     * @param companyAllowanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompanyAllowanceDTO> partialUpdate(CompanyAllowanceDTO companyAllowanceDTO);

    /**
     * Get all the companyAllowances.
     *
     * @return the list of entities.
     */
    List<CompanyAllowanceDTO> findAll();

    /**
     * Get the "id" companyAllowance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompanyAllowanceDTO> findOne(Long id);

    /**
     * Delete the "id" companyAllowance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
