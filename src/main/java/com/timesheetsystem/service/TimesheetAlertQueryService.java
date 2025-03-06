package com.timesheetsystem.service;

import com.timesheetsystem.domain.*; // for static metamodels
import com.timesheetsystem.domain.TimesheetAlert;
import com.timesheetsystem.repository.TimesheetAlertRepository;
import com.timesheetsystem.service.criteria.TimesheetAlertCriteria;
import com.timesheetsystem.service.dto.TimesheetAlertDTO;
import com.timesheetsystem.service.mapper.TimesheetAlertMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TimesheetAlert} entities in the database.
 * The main input is a {@link TimesheetAlertCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TimesheetAlertDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TimesheetAlertQueryService extends QueryService<TimesheetAlert> {

    private static final Logger log = LoggerFactory.getLogger(TimesheetAlertQueryService.class);

    private final TimesheetAlertRepository timesheetAlertRepository;

    private final TimesheetAlertMapper timesheetAlertMapper;

    public TimesheetAlertQueryService(TimesheetAlertRepository timesheetAlertRepository, TimesheetAlertMapper timesheetAlertMapper) {
        this.timesheetAlertRepository = timesheetAlertRepository;
        this.timesheetAlertMapper = timesheetAlertMapper;
    }

    /**
     * Return a {@link Page} of {@link TimesheetAlertDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TimesheetAlertDTO> findByCriteria(TimesheetAlertCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TimesheetAlert> specification = createSpecification(criteria);
        return timesheetAlertRepository.findAll(specification, page).map(timesheetAlertMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TimesheetAlertCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TimesheetAlert> specification = createSpecification(criteria);
        return timesheetAlertRepository.count(specification);
    }

    /**
     * Function to convert {@link TimesheetAlertCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TimesheetAlert> createSpecification(TimesheetAlertCriteria criteria) {
        Specification<TimesheetAlert> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TimesheetAlert_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), TimesheetAlert_.userId));
            }
            if (criteria.getTimesheetId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimesheetId(), TimesheetAlert_.timesheetId));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), TimesheetAlert_.date));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), TimesheetAlert_.type));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), TimesheetAlert_.message));
            }
            if (criteria.getSeverity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSeverity(), TimesheetAlert_.severity));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), TimesheetAlert_.status));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TimesheetAlert_.createdAt));
            }
            if (criteria.getResolvedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getResolvedAt(), TimesheetAlert_.resolvedAt));
            }
            if (criteria.getResolution() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResolution(), TimesheetAlert_.resolution));
            }
            if (criteria.getResolvedById() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getResolvedById(),
                        root -> root.join(TimesheetAlert_.resolvedBy, JoinType.LEFT).get(User_.id)
                    )
                );
            }
        }
        return specification;
    }
}
