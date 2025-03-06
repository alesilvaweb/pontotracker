package com.timesheetsystem.service;

import com.timesheetsystem.service.dto.SystemConfigDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.timesheetsystem.domain.SystemConfig}.
 */
public interface SystemConfigService {
    /**
     * Save a systemConfig.
     *
     * @param systemConfigDTO the entity to save.
     * @return the persisted entity.
     */
    SystemConfigDTO save(SystemConfigDTO systemConfigDTO);

    /**
     * Updates a systemConfig.
     *
     * @param systemConfigDTO the entity to update.
     * @return the persisted entity.
     */
    SystemConfigDTO update(SystemConfigDTO systemConfigDTO);

    /**
     * Partially updates a systemConfig.
     *
     * @param systemConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SystemConfigDTO> partialUpdate(SystemConfigDTO systemConfigDTO);

    /**
     * Get all the systemConfigs.
     *
     * @return the list of entities.
     */
    List<SystemConfigDTO> findAll();

    /**
     * Get the "id" systemConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemConfigDTO> findOne(Long id);

    /**
     * Delete the "id" systemConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
