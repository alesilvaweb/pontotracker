package com.timesheetsystem.web.rest;

import com.timesheetsystem.repository.TimesheetAuditRepository;
import com.timesheetsystem.service.TimesheetAuditService;
import com.timesheetsystem.service.dto.TimesheetAuditDTO;
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
 * REST controller for managing {@link com.timesheetsystem.domain.TimesheetAudit}.
 */
@RestController
@RequestMapping("/api/timesheet-audits")
public class TimesheetAuditResource {

    private static final Logger log = LoggerFactory.getLogger(TimesheetAuditResource.class);

    private static final String ENTITY_NAME = "timesheetAudit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimesheetAuditService timesheetAuditService;

    private final TimesheetAuditRepository timesheetAuditRepository;

    public TimesheetAuditResource(TimesheetAuditService timesheetAuditService, TimesheetAuditRepository timesheetAuditRepository) {
        this.timesheetAuditService = timesheetAuditService;
        this.timesheetAuditRepository = timesheetAuditRepository;
    }

    /**
     * {@code POST  /timesheet-audits} : Create a new timesheetAudit.
     *
     * @param timesheetAuditDTO the timesheetAuditDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timesheetAuditDTO, or with status {@code 400 (Bad Request)} if the timesheetAudit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TimesheetAuditDTO> createTimesheetAudit(@Valid @RequestBody TimesheetAuditDTO timesheetAuditDTO)
        throws URISyntaxException {
        log.debug("REST request to save TimesheetAudit : {}", timesheetAuditDTO);
        if (timesheetAuditDTO.getId() != null) {
            throw new BadRequestAlertException("A new timesheetAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        timesheetAuditDTO = timesheetAuditService.save(timesheetAuditDTO);
        return ResponseEntity.created(new URI("/api/timesheet-audits/" + timesheetAuditDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, timesheetAuditDTO.getId().toString()))
            .body(timesheetAuditDTO);
    }

    /**
     * {@code PUT  /timesheet-audits/:id} : Updates an existing timesheetAudit.
     *
     * @param id the id of the timesheetAuditDTO to save.
     * @param timesheetAuditDTO the timesheetAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timesheetAuditDTO,
     * or with status {@code 400 (Bad Request)} if the timesheetAuditDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timesheetAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TimesheetAuditDTO> updateTimesheetAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TimesheetAuditDTO timesheetAuditDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TimesheetAudit : {}, {}", id, timesheetAuditDTO);
        if (timesheetAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timesheetAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timesheetAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        timesheetAuditDTO = timesheetAuditService.update(timesheetAuditDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timesheetAuditDTO.getId().toString()))
            .body(timesheetAuditDTO);
    }

    /**
     * {@code PATCH  /timesheet-audits/:id} : Partial updates given fields of an existing timesheetAudit, field will ignore if it is null
     *
     * @param id the id of the timesheetAuditDTO to save.
     * @param timesheetAuditDTO the timesheetAuditDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timesheetAuditDTO,
     * or with status {@code 400 (Bad Request)} if the timesheetAuditDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timesheetAuditDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timesheetAuditDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TimesheetAuditDTO> partialUpdateTimesheetAudit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TimesheetAuditDTO timesheetAuditDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimesheetAudit partially : {}, {}", id, timesheetAuditDTO);
        if (timesheetAuditDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timesheetAuditDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timesheetAuditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimesheetAuditDTO> result = timesheetAuditService.partialUpdate(timesheetAuditDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timesheetAuditDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /timesheet-audits} : get all the timesheetAudits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timesheetAudits in body.
     */
    @GetMapping("")
    public List<TimesheetAuditDTO> getAllTimesheetAudits() {
        log.debug("REST request to get all TimesheetAudits");
        return timesheetAuditService.findAll();
    }

    /**
     * {@code GET  /timesheet-audits/:id} : get the "id" timesheetAudit.
     *
     * @param id the id of the timesheetAuditDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timesheetAuditDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TimesheetAuditDTO> getTimesheetAudit(@PathVariable("id") Long id) {
        log.debug("REST request to get TimesheetAudit : {}", id);
        Optional<TimesheetAuditDTO> timesheetAuditDTO = timesheetAuditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timesheetAuditDTO);
    }

    /**
     * {@code DELETE  /timesheet-audits/:id} : delete the "id" timesheetAudit.
     *
     * @param id the id of the timesheetAuditDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimesheetAudit(@PathVariable("id") Long id) {
        log.debug("REST request to delete TimesheetAudit : {}", id);
        timesheetAuditService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
