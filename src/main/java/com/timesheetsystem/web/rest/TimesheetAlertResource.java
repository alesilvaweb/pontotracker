package com.timesheetsystem.web.rest;

import com.timesheetsystem.repository.TimesheetAlertRepository;
import com.timesheetsystem.service.TimesheetAlertQueryService;
import com.timesheetsystem.service.TimesheetAlertService;
import com.timesheetsystem.service.criteria.TimesheetAlertCriteria;
import com.timesheetsystem.service.dto.TimesheetAlertDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.timesheetsystem.domain.TimesheetAlert}.
 */
@RestController
@RequestMapping("/api/timesheet-alerts")
public class TimesheetAlertResource {

    private static final Logger log = LoggerFactory.getLogger(TimesheetAlertResource.class);

    private static final String ENTITY_NAME = "timesheetAlert";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimesheetAlertService timesheetAlertService;

    private final TimesheetAlertRepository timesheetAlertRepository;

    private final TimesheetAlertQueryService timesheetAlertQueryService;

    public TimesheetAlertResource(
        TimesheetAlertService timesheetAlertService,
        TimesheetAlertRepository timesheetAlertRepository,
        TimesheetAlertQueryService timesheetAlertQueryService
    ) {
        this.timesheetAlertService = timesheetAlertService;
        this.timesheetAlertRepository = timesheetAlertRepository;
        this.timesheetAlertQueryService = timesheetAlertQueryService;
    }

    /**
     * {@code POST  /timesheet-alerts} : Create a new timesheetAlert.
     *
     * @param timesheetAlertDTO the timesheetAlertDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timesheetAlertDTO, or with status {@code 400 (Bad Request)} if the timesheetAlert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TimesheetAlertDTO> createTimesheetAlert(@Valid @RequestBody TimesheetAlertDTO timesheetAlertDTO)
        throws URISyntaxException {
        log.debug("REST request to save TimesheetAlert : {}", timesheetAlertDTO);
        if (timesheetAlertDTO.getId() != null) {
            throw new BadRequestAlertException("A new timesheetAlert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        timesheetAlertDTO = timesheetAlertService.save(timesheetAlertDTO);
        return ResponseEntity.created(new URI("/api/timesheet-alerts/" + timesheetAlertDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, timesheetAlertDTO.getId().toString()))
            .body(timesheetAlertDTO);
    }

    /**
     * {@code PUT  /timesheet-alerts/:id} : Updates an existing timesheetAlert.
     *
     * @param id the id of the timesheetAlertDTO to save.
     * @param timesheetAlertDTO the timesheetAlertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timesheetAlertDTO,
     * or with status {@code 400 (Bad Request)} if the timesheetAlertDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timesheetAlertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TimesheetAlertDTO> updateTimesheetAlert(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TimesheetAlertDTO timesheetAlertDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TimesheetAlert : {}, {}", id, timesheetAlertDTO);
        if (timesheetAlertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timesheetAlertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timesheetAlertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        timesheetAlertDTO = timesheetAlertService.update(timesheetAlertDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timesheetAlertDTO.getId().toString()))
            .body(timesheetAlertDTO);
    }

    /**
     * {@code PATCH  /timesheet-alerts/:id} : Partial updates given fields of an existing timesheetAlert, field will ignore if it is null
     *
     * @param id the id of the timesheetAlertDTO to save.
     * @param timesheetAlertDTO the timesheetAlertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timesheetAlertDTO,
     * or with status {@code 400 (Bad Request)} if the timesheetAlertDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timesheetAlertDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timesheetAlertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TimesheetAlertDTO> partialUpdateTimesheetAlert(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TimesheetAlertDTO timesheetAlertDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimesheetAlert partially : {}, {}", id, timesheetAlertDTO);
        if (timesheetAlertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timesheetAlertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timesheetAlertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimesheetAlertDTO> result = timesheetAlertService.partialUpdate(timesheetAlertDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timesheetAlertDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /timesheet-alerts} : get all the timesheetAlerts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timesheetAlerts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TimesheetAlertDTO>> getAllTimesheetAlerts(
        TimesheetAlertCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TimesheetAlerts by criteria: {}", criteria);

        Page<TimesheetAlertDTO> page = timesheetAlertQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /timesheet-alerts/count} : count all the timesheetAlerts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTimesheetAlerts(TimesheetAlertCriteria criteria) {
        log.debug("REST request to count TimesheetAlerts by criteria: {}", criteria);
        return ResponseEntity.ok().body(timesheetAlertQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /timesheet-alerts/:id} : get the "id" timesheetAlert.
     *
     * @param id the id of the timesheetAlertDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timesheetAlertDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TimesheetAlertDTO> getTimesheetAlert(@PathVariable("id") Long id) {
        log.debug("REST request to get TimesheetAlert : {}", id);
        Optional<TimesheetAlertDTO> timesheetAlertDTO = timesheetAlertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timesheetAlertDTO);
    }

    /**
     * {@code DELETE  /timesheet-alerts/:id} : delete the "id" timesheetAlert.
     *
     * @param id the id of the timesheetAlertDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimesheetAlert(@PathVariable("id") Long id) {
        log.debug("REST request to delete TimesheetAlert : {}", id);
        timesheetAlertService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
