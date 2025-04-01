package com.timesheetsystem.web.rest;

import com.timesheetsystem.repository.UserPreferenceRepository;
import com.timesheetsystem.service.UserPreferenceService;
import com.timesheetsystem.service.dto.UserPreferenceDTO;
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
 * REST controller for managing {@link com.timesheetsystem.domain.UserPreference}.
 */
@RestController
@RequestMapping("/api/user-preferences")
public class UserPreferenceResource {

    private static final Logger log = LoggerFactory.getLogger(UserPreferenceResource.class);

    private static final String ENTITY_NAME = "userPreference";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserPreferenceService userPreferenceService;

    private final UserPreferenceRepository userPreferenceRepository;

    public UserPreferenceResource(UserPreferenceService userPreferenceService, UserPreferenceRepository userPreferenceRepository) {
        this.userPreferenceService = userPreferenceService;
        this.userPreferenceRepository = userPreferenceRepository;
    }

    /**
     * {@code POST  /user-preferences} : Create a new userPreference.
     *
     * @param userPreferenceDTO the userPreferenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userPreferenceDTO, or with status {@code 400 (Bad Request)} if the userPreference has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserPreferenceDTO> createUserPreference(@Valid @RequestBody UserPreferenceDTO userPreferenceDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserPreference : {}", userPreferenceDTO);
        if (userPreferenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new userPreference cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userPreferenceDTO = userPreferenceService.save(userPreferenceDTO);
        return ResponseEntity.created(new URI("/api/user-preferences/" + userPreferenceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userPreferenceDTO.getId().toString()))
            .body(userPreferenceDTO);
    }

    /**
     * {@code PUT  /user-preferences/:id} : Updates an existing userPreference.
     *
     * @param id the id of the userPreferenceDTO to save.
     * @param userPreferenceDTO the userPreferenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPreferenceDTO,
     * or with status {@code 400 (Bad Request)} if the userPreferenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userPreferenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserPreferenceDTO> updateUserPreference(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserPreferenceDTO userPreferenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserPreference : {}, {}", id, userPreferenceDTO);
        if (userPreferenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPreferenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPreferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userPreferenceDTO = userPreferenceService.update(userPreferenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPreferenceDTO.getId().toString()))
            .body(userPreferenceDTO);
    }

    /**
     * {@code PATCH  /user-preferences/:id} : Partial updates given fields of an existing userPreference, field will ignore if it is null
     *
     * @param id the id of the userPreferenceDTO to save.
     * @param userPreferenceDTO the userPreferenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userPreferenceDTO,
     * or with status {@code 400 (Bad Request)} if the userPreferenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userPreferenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userPreferenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserPreferenceDTO> partialUpdateUserPreference(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserPreferenceDTO userPreferenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserPreference partially : {}, {}", id, userPreferenceDTO);
        if (userPreferenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userPreferenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userPreferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserPreferenceDTO> result = userPreferenceService.partialUpdate(userPreferenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userPreferenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-preferences} : get all the userPreferences.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userPreferences in body.
     */
    @GetMapping("")
    public List<UserPreferenceDTO> getAllUserPreferences(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all UserPreferences");
        return userPreferenceService.findAll();
    }

    /**
     * {@code GET  /user-preferences/:id} : get the "id" userPreference.
     *
     * @param id the id of the userPreferenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userPreferenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserPreferenceDTO> getUserPreference(@PathVariable("id") Long id) {
        log.debug("REST request to get UserPreference : {}", id);
        Optional<UserPreferenceDTO> userPreferenceDTO = userPreferenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userPreferenceDTO);
    }

    /**
     * {@code DELETE  /user-preferences/:id} : delete the "id" userPreference.
     *
     * @param id the id of the userPreferenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserPreference(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserPreference : {}", id);
        userPreferenceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
