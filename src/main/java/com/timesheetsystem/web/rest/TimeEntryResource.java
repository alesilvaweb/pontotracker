package com.timesheetsystem.web.rest;

import com.timesheetsystem.repository.TimeEntryRepository;
import com.timesheetsystem.service.TimeEntryQueryService;
import com.timesheetsystem.service.TimeEntryService;
import com.timesheetsystem.service.criteria.TimeEntryCriteria;
import com.timesheetsystem.service.dto.TimeEntryDTO;
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
 * REST controller for managing {@link com.timesheetsystem.domain.TimeEntry}.
 */
@RestController
@RequestMapping("/api/time-entries")
public class TimeEntryResource {

    private static final Logger log = LoggerFactory.getLogger(TimeEntryResource.class);

    private static final String ENTITY_NAME = "timeEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeEntryService timeEntryService;

    private final TimeEntryRepository timeEntryRepository;

    private final TimeEntryQueryService timeEntryQueryService;

    public TimeEntryResource(
        TimeEntryService timeEntryService,
        TimeEntryRepository timeEntryRepository,
        TimeEntryQueryService timeEntryQueryService
    ) {
        this.timeEntryService = timeEntryService;
        this.timeEntryRepository = timeEntryRepository;
        this.timeEntryQueryService = timeEntryQueryService;
    }

    /**
     * {@code POST  /time-entries} : Create a new timeEntry.
     *
     * @param timeEntryDTO the timeEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timeEntryDTO, or with status {@code 400 (Bad Request)} if the timeEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TimeEntryDTO> createTimeEntry(@Valid @RequestBody TimeEntryDTO timeEntryDTO) throws URISyntaxException {
        log.debug("REST request to save TimeEntry : {}", timeEntryDTO);
        if (timeEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new timeEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        timeEntryDTO = timeEntryService.save(timeEntryDTO);
        return ResponseEntity.created(new URI("/api/time-entries/" + timeEntryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, timeEntryDTO.getId().toString()))
            .body(timeEntryDTO);
    }

    /**
     * {@code PUT  /time-entries/:id} : Updates an existing timeEntry.
     *
     * @param id the id of the timeEntryDTO to save.
     * @param timeEntryDTO the timeEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeEntryDTO,
     * or with status {@code 400 (Bad Request)} if the timeEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timeEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TimeEntryDTO> updateTimeEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TimeEntryDTO timeEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TimeEntry : {}, {}", id, timeEntryDTO);
        if (timeEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        timeEntryDTO = timeEntryService.update(timeEntryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timeEntryDTO.getId().toString()))
            .body(timeEntryDTO);
    }

    /**
     * {@code PATCH  /time-entries/:id} : Partial updates given fields of an existing timeEntry, field will ignore if it is null
     *
     * @param id the id of the timeEntryDTO to save.
     * @param timeEntryDTO the timeEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeEntryDTO,
     * or with status {@code 400 (Bad Request)} if the timeEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timeEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timeEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TimeEntryDTO> partialUpdateTimeEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TimeEntryDTO timeEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimeEntry partially : {}, {}", id, timeEntryDTO);
        if (timeEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimeEntryDTO> result = timeEntryService.partialUpdate(timeEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, timeEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /time-entries} : get all the timeEntries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timeEntries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TimeEntryDTO>> getAllTimeEntries(
        TimeEntryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TimeEntries by criteria: {}", criteria);

        Page<TimeEntryDTO> page = timeEntryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /time-entries/count} : count all the timeEntries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTimeEntries(TimeEntryCriteria criteria) {
        log.debug("REST request to count TimeEntries by criteria: {}", criteria);
        return ResponseEntity.ok().body(timeEntryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /time-entries/:id} : get the "id" timeEntry.
     *
     * @param id the id of the timeEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TimeEntryDTO> getTimeEntry(@PathVariable("id") Long id) {
        log.debug("REST request to get TimeEntry : {}", id);
        Optional<TimeEntryDTO> timeEntryDTO = timeEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeEntryDTO);
    }

    /**
     * {@code DELETE  /time-entries/:id} : delete the "id" timeEntry.
     *
     * @param id the id of the timeEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeEntry(@PathVariable("id") Long id) {
        log.debug("REST request to delete TimeEntry : {}", id);
        timeEntryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
