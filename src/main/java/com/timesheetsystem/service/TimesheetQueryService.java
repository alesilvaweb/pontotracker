package com.timesheetsystem.service;

import com.timesheetsystem.domain.*; // for static metamodels
import com.timesheetsystem.domain.Timesheet;
import com.timesheetsystem.repository.TimesheetRepository;
import com.timesheetsystem.service.criteria.TimesheetCriteria;
import com.timesheetsystem.service.dto.TimesheetDTO;
import com.timesheetsystem.service.mapper.TimesheetMapper;
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
 * Service for executing complex queries for {@link Timesheet} entities in the database.
 * The main input is a {@link TimesheetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TimesheetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TimesheetQueryService extends QueryService<Timesheet> {

    private static final Logger log = LoggerFactory.getLogger(TimesheetQueryService.class);

    private final TimesheetRepository timesheetRepository;

    private final TimesheetMapper timesheetMapper;

    public TimesheetQueryService(TimesheetRepository timesheetRepository, TimesheetMapper timesheetMapper) {
        this.timesheetRepository = timesheetRepository;
        this.timesheetMapper = timesheetMapper;
    }

    /**
     * Return a {@link Page} of {@link TimesheetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TimesheetDTO> findByCriteria(TimesheetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Timesheet> specification = createSpecification(criteria);
        return timesheetRepository.findAll(specification, page).map(timesheetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TimesheetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Timesheet> specification = createSpecification(criteria);
        return timesheetRepository.count(specification);
    }

    /**
     * Function to convert {@link TimesheetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Timesheet> createSpecification(TimesheetCriteria criteria) {
        Specification<Timesheet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Timesheet_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Timesheet_.date));
            }
            if (criteria.getModality() != null) {
                specification = specification.and(buildSpecification(criteria.getModality(), Timesheet_.modality));
            }
            if (criteria.getClassification() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClassification(), Timesheet_.classification));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Timesheet_.description));
            }
            if (criteria.getTotalHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalHours(), Timesheet_.totalHours));
            }
            if (criteria.getOvertimeHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOvertimeHours(), Timesheet_.overtimeHours));
            }
            if (criteria.getAllowanceValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAllowanceValue(), Timesheet_.allowanceValue));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Timesheet_.status));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Timesheet_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Timesheet_.updatedAt));
            }
            if (criteria.getApprovedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApprovedAt(), Timesheet_.approvedAt));
            }
            if (criteria.getApprovedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApprovedBy(), Timesheet_.approvedBy));
            }
            if (criteria.getTimeEntryId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getTimeEntryId(),
                        root -> root.join(Timesheet_.timeEntries, JoinType.LEFT).get(TimeEntry_.id)
                    )
                );
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(Timesheet_.user, JoinType.LEFT).get(User_.id))
                );
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCompanyId(), root -> root.join(Timesheet_.company, JoinType.LEFT).get(Company_.id))
                );
            }
        }
        return specification;
    }
}
