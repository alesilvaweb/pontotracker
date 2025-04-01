package com.timesheetsystem.web.rest;

import com.timesheetsystem.repository.TimesheetRepository;
import com.timesheetsystem.service.TimesheetQueryService;
import com.timesheetsystem.service.TimesheetService;
import com.timesheetsystem.service.criteria.TimesheetCriteria;
import com.timesheetsystem.service.dto.TimesheetDTO;
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
 * REST controller for managing {@link com.timesheetsystem.domain.Timesheet}.
 */
@RestController
@RequestMapping("/api/timesheets")
public class TimesheetResource {

    private static final Logger log = LoggerFactory.getLogger(TimesheetResource.class);

    private static final String ENTITY_NAME = "timesheet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimesheetService timesheetService;

    private final TimesheetRepository timesheetRepository;

    private final TimesheetQueryService timesheetQueryService;

    public TimesheetResource(
        TimesheetService timesheetService,
        TimesheetRepository timesheetRepository,
        TimesheetQueryService timesheetQueryService
    ) {
        this.timesheetService = timesheetService;
        this.timesheetRepository = timesheetRepository;
        this.timesheetQueryService = timesheetQueryService;
    }

    /**
     * {@code POST  /timesheets} : Create a new timesheet.
     *
     * @param timesheetDTO the timesheetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timesheetDTO, or with status {@code 400 (Bad Request)} if the timesheet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TimesheetDTO> createTimesheet(@Valid @RequestBody TimesheetDTO timesheetDTO) throws URISyntaxException {
        log.debug("REST request to save Timesheet : {}", timesheetDTO);
        if (timesheetDTO.getId() != null) {
            throw new BadRequestAlertException("A new timesheet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        timesheetDTO = timesheetService.save(timesheetDTO);
        return ResponseEntity.created(new URI("/api/timesheets/" + timesheetDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, timesheetDTO.getId().toString()))
            .body(timesheetDTO);
    }

    /**
     * {@code PUT  /timesheets/:id} : Updates an existing timesheet.
     *
     * @param id the id of the timesheetDTO to save.
     * @param timesheetDTO the timesheetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timesheetDTO,
     * or with status {@code 400 (Bad Request)} if the timesheetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timesheetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TimesheetDTO> updateTimesheet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TimesheetDTO timesheetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Timesheet : {}, {}", id, timesheetDTO);
        if (timesheetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timesheetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timesheetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        timesheetDTO = timesheetService.update(timesheetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timesheetDTO.getId().toString()))
            .body(timesheetDTO);
    }

    /**
     * {@code PATCH  /timesheets/:id} : Partial updates given fields of an existing timesheet, field will ignore if it is null
     *
     * @param id the id of the timesheetDTO to save.
     * @param timesheetDTO the timesheetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timesheetDTO,
     * or with status {@code 400 (Bad Request)} if the timesheetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timesheetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timesheetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TimesheetDTO> partialUpdateTimesheet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TimesheetDTO timesheetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Timesheet partially : {}, {}", id, timesheetDTO);
        if (timesheetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timesheetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timesheetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimesheetDTO> result = timesheetService.partialUpdate(timesheetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timesheetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /timesheets} : get all the timesheets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timesheets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TimesheetDTO>> getAllTimesheets(
        TimesheetCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Timesheets by criteria: {}", criteria);

        Page<TimesheetDTO> page = timesheetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /timesheets/count} : count all the timesheets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTimesheets(TimesheetCriteria criteria) {
        log.debug("REST request to count Timesheets by criteria: {}", criteria);
        return ResponseEntity.ok().body(timesheetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /timesheets/:id} : get the "id" timesheet.
     *
     * @param id the id of the timesheetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timesheetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TimesheetDTO> getTimesheet(@PathVariable("id") Long id) {
        log.debug("REST request to get Timesheet : {}", id);
        Optional<TimesheetDTO> timesheetDTO = timesheetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timesheetDTO);
    }

    /**
     * {@code DELETE  /timesheets/:id} : delete the "id" timesheet.
     *
     * @param id the id of the timesheetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimesheet(@PathVariable("id") Long id) {
        log.debug("REST request to delete Timesheet : {}", id);
        timesheetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
