package com.timesheetsystem.web.rest;

import com.timesheetsystem.repository.CompanyAllowanceRepository;
import com.timesheetsystem.service.CompanyAllowanceService;
import com.timesheetsystem.service.dto.CompanyAllowanceDTO;
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
 * REST controller for managing {@link com.timesheetsystem.domain.CompanyAllowance}.
 */
@RestController
@RequestMapping("/api/company-allowances")
public class CompanyAllowanceResource {

    private static final Logger log = LoggerFactory.getLogger(CompanyAllowanceResource.class);

    private static final String ENTITY_NAME = "companyAllowance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyAllowanceService companyAllowanceService;

    private final CompanyAllowanceRepository companyAllowanceRepository;

    public CompanyAllowanceResource(
        CompanyAllowanceService companyAllowanceService,
        CompanyAllowanceRepository companyAllowanceRepository
    ) {
        this.companyAllowanceService = companyAllowanceService;
        this.companyAllowanceRepository = companyAllowanceRepository;
    }

    /**
     * {@code POST  /company-allowances} : Create a new companyAllowance.
     *
     * @param companyAllowanceDTO the companyAllowanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyAllowanceDTO, or with status {@code 400 (Bad Request)} if the companyAllowance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CompanyAllowanceDTO> createCompanyAllowance(@Valid @RequestBody CompanyAllowanceDTO companyAllowanceDTO)
        throws URISyntaxException {
        log.debug("REST request to save CompanyAllowance : {}", companyAllowanceDTO);
        if (companyAllowanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new companyAllowance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        companyAllowanceDTO = companyAllowanceService.save(companyAllowanceDTO);
        return ResponseEntity.created(new URI("/api/company-allowances/" + companyAllowanceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, companyAllowanceDTO.getId().toString()))
            .body(companyAllowanceDTO);
    }

    /**
     * {@code PUT  /company-allowances/:id} : Updates an existing companyAllowance.
     *
     * @param id the id of the companyAllowanceDTO to save.
     * @param companyAllowanceDTO the companyAllowanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyAllowanceDTO,
     * or with status {@code 400 (Bad Request)} if the companyAllowanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyAllowanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyAllowanceDTO> updateCompanyAllowance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CompanyAllowanceDTO companyAllowanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompanyAllowance : {}, {}", id, companyAllowanceDTO);
        if (companyAllowanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyAllowanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyAllowanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        companyAllowanceDTO = companyAllowanceService.update(companyAllowanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyAllowanceDTO.getId().toString()))
            .body(companyAllowanceDTO);
    }

    /**
     * {@code PATCH  /company-allowances/:id} : Partial updates given fields of an existing companyAllowance, field will ignore if it is null
     *
     * @param id the id of the companyAllowanceDTO to save.
     * @param companyAllowanceDTO the companyAllowanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyAllowanceDTO,
     * or with status {@code 400 (Bad Request)} if the companyAllowanceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the companyAllowanceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the companyAllowanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompanyAllowanceDTO> partialUpdateCompanyAllowance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CompanyAllowanceDTO companyAllowanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompanyAllowance partially : {}, {}", id, companyAllowanceDTO);
        if (companyAllowanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, companyAllowanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyAllowanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompanyAllowanceDTO> result = companyAllowanceService.partialUpdate(companyAllowanceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyAllowanceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /company-allowances} : get all the companyAllowances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyAllowances in body.
     */
    @GetMapping("")
    public List<CompanyAllowanceDTO> getAllCompanyAllowances() {
        log.debug("REST request to get all CompanyAllowances");
        return companyAllowanceService.findAll();
    }

    /**
     * {@code GET  /company-allowances/:id} : get the "id" companyAllowance.
     *
     * @param id the id of the companyAllowanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyAllowanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyAllowanceDTO> getCompanyAllowance(@PathVariable("id") Long id) {
        log.debug("REST request to get CompanyAllowance : {}", id);
        Optional<CompanyAllowanceDTO> companyAllowanceDTO = companyAllowanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(companyAllowanceDTO);
    }

    /**
     * {@code DELETE  /company-allowances/:id} : delete the "id" companyAllowance.
     *
     * @param id the id of the companyAllowanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompanyAllowance(@PathVariable("id") Long id) {
        log.debug("REST request to delete CompanyAllowance : {}", id);
        companyAllowanceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
