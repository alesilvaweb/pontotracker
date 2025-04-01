package com.timesheetsystem.web.rest;

import com.timesheetsystem.repository.TimesheetReportRepository;
import com.timesheetsystem.service.TimesheetReportQueryService;
import com.timesheetsystem.service.TimesheetReportService;
import com.timesheetsystem.service.criteria.TimesheetReportCriteria;
import com.timesheetsystem.service.dto.TimesheetReportDTO;
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
 * REST controller for managing {@link com.timesheetsystem.domain.TimesheetReport}.
 */
@RestController
@RequestMapping("/api/timesheet-reports")
public class TimesheetReportResource {

    private static final Logger log = LoggerFactory.getLogger(TimesheetReportResource.class);

    private static final String ENTITY_NAME = "timesheetReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimesheetReportService timesheetReportService;

    private final TimesheetReportRepository timesheetReportRepository;

    private final TimesheetReportQueryService timesheetReportQueryService;

    public TimesheetReportResource(
        TimesheetReportService timesheetReportService,
        TimesheetReportRepository timesheetReportRepository,
        TimesheetReportQueryService timesheetReportQueryService
    ) {
        this.timesheetReportService = timesheetReportService;
        this.timesheetReportRepository = timesheetReportRepository;
        this.timesheetReportQueryService = timesheetReportQueryService;
    }

    /**
     * {@code POST  /timesheet-reports} : Create a new timesheetReport.
     *
     * @param timesheetReportDTO the timesheetReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timesheetReportDTO, or with status {@code 400 (Bad Request)} if the timesheetReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TimesheetReportDTO> createTimesheetReport(@Valid @RequestBody TimesheetReportDTO timesheetReportDTO)
        throws URISyntaxException {
        log.debug("REST request to save TimesheetReport : {}", timesheetReportDTO);
        if (timesheetReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new timesheetReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        timesheetReportDTO = timesheetReportService.save(timesheetReportDTO);
        return ResponseEntity.created(new URI("/api/timesheet-reports/" + timesheetReportDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, timesheetReportDTO.getId().toString()))
            .body(timesheetReportDTO);
    }

    /**
     * {@code PUT  /timesheet-reports/:id} : Updates an existing timesheetReport.
     *
     * @param id the id of the timesheetReportDTO to save.
     * @param timesheetReportDTO the timesheetReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timesheetReportDTO,
     * or with status {@code 400 (Bad Request)} if the timesheetReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timesheetReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TimesheetReportDTO> updateTimesheetReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TimesheetReportDTO timesheetReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TimesheetReport : {}, {}", id, timesheetReportDTO);
        if (timesheetReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timesheetReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timesheetReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        timesheetReportDTO = timesheetReportService.update(timesheetReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timesheetReportDTO.getId().toString()))
            .body(timesheetReportDTO);
    }

    /**
     * {@code PATCH  /timesheet-reports/:id} : Partial updates given fields of an existing timesheetReport, field will ignore if it is null
     *
     * @param id the id of the timesheetReportDTO to save.
     * @param timesheetReportDTO the timesheetReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timesheetReportDTO,
     * or with status {@code 400 (Bad Request)} if the timesheetReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timesheetReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timesheetReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TimesheetReportDTO> partialUpdateTimesheetReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TimesheetReportDTO timesheetReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimesheetReport partially : {}, {}", id, timesheetReportDTO);
        if (timesheetReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timesheetReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timesheetReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimesheetReportDTO> result = timesheetReportService.partialUpdate(timesheetReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timesheetReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /timesheet-reports} : get all the timesheetReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timesheetReports in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TimesheetReportDTO>> getAllTimesheetReports(
        TimesheetReportCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TimesheetReports by criteria: {}", criteria);

        Page<TimesheetReportDTO> page = timesheetReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /timesheet-reports/count} : count all the timesheetReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTimesheetReports(TimesheetReportCriteria criteria) {
        log.debug("REST request to count TimesheetReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(timesheetReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /timesheet-reports/:id} : get the "id" timesheetReport.
     *
     * @param id the id of the timesheetReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timesheetReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TimesheetReportDTO> getTimesheetReport(@PathVariable("id") Long id) {
        log.debug("REST request to get TimesheetReport : {}", id);
        Optional<TimesheetReportDTO> timesheetReportDTO = timesheetReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timesheetReportDTO);
    }

    /**
     * {@code DELETE  /timesheet-reports/:id} : delete the "id" timesheetReport.
     *
     * @param id the id of the timesheetReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimesheetReport(@PathVariable("id") Long id) {
        log.debug("REST request to delete TimesheetReport : {}", id);
        timesheetReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
