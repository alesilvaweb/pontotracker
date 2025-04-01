package com.timesheetsystem.web.rest;

import com.timesheetsystem.repository.SystemConfigRepository;
import com.timesheetsystem.service.SystemConfigService;
import com.timesheetsystem.service.dto.SystemConfigDTO;
import com.timesheetsystem.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.timesheetsystem.domain.SystemConfig}.
 */
@RestController
@RequestMapping("/api/system-configs")
public class SystemConfigResource {

    private static final Logger log = LoggerFactory.getLogger(SystemConfigResource.class);

    private static final String ENTITY_NAME = "systemConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemConfigService systemConfigService;

    private final SystemConfigRepository systemConfigRepository;

    public SystemConfigResource(SystemConfigService systemConfigService, SystemConfigRepository systemConfigRepository) {
        this.systemConfigService = systemConfigService;
        this.systemConfigRepository = systemConfigRepository;
    }

    /**
     * {@code POST  /system-configs} : Create a new systemConfig.
     *
     * @param systemConfigDTO the systemConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemConfigDTO, or with status {@code 400 (Bad Request)} if the systemConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SystemConfigDTO> createSystemConfig(@Valid @RequestBody SystemConfigDTO systemConfigDTO)
        throws URISyntaxException {
        log.debug("REST request to save SystemConfig : {}", systemConfigDTO);
        if (systemConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        systemConfigDTO = systemConfigService.save(systemConfigDTO);
        return ResponseEntity.created(new URI("/api/system-configs/" + systemConfigDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, systemConfigDTO.getId().toString()))
            .body(systemConfigDTO);
    }

    /**
     * {@code PUT  /system-configs/:id} : Updates an existing systemConfig.
     *
     * @param id the id of the systemConfigDTO to save.
     * @param systemConfigDTO the systemConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemConfigDTO,
     * or with status {@code 400 (Bad Request)} if the systemConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SystemConfigDTO> updateSystemConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemConfigDTO systemConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SystemConfig : {}, {}", id, systemConfigDTO);
        if (systemConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        systemConfigDTO = systemConfigService.update(systemConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemConfigDTO.getId().toString()))
            .body(systemConfigDTO);
    }

    /**
     * {@code PATCH  /system-configs/:id} : Partial updates given fields of an existing systemConfig, field will ignore if it is null
     *
     * @param id the id of the systemConfigDTO to save.
     * @param systemConfigDTO the systemConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemConfigDTO,
     * or with status {@code 400 (Bad Request)} if the systemConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemConfigDTO> partialUpdateSystemConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemConfigDTO systemConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemConfig partially : {}, {}", id, systemConfigDTO);
        if (systemConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemConfigDTO> result = systemConfigService.partialUpdate(systemConfigDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemConfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-configs} : get all the systemConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemConfigs in body.
     */
    @GetMapping("")
    public List<SystemConfigDTO> getAllSystemConfigs() {
        log.debug("REST request to get all SystemConfigs");
        return systemConfigService.findAll();
    }

    /**
     * {@code GET  /system-configs/:id} : get the "id" systemConfig.
     *
     * @param id the id of the systemConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SystemConfigDTO> getSystemConfig(@PathVariable("id") Long id) {
        log.debug("REST request to get SystemConfig : {}", id);
        Optional<SystemConfigDTO> systemConfigDTO = systemConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemConfigDTO);
    }

    /**
     * {@code DELETE  /system-configs/:id} : delete the "id" systemConfig.
     *
     * @param id the id of the systemConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemConfig(@PathVariable("id") Long id) {
        log.debug("REST request to delete SystemConfig : {}", id);
        systemConfigService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
