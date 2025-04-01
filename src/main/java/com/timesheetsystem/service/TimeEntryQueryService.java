package com.timesheetsystem.service;

import com.timesheetsystem.domain.*; // for static metamodels
import com.timesheetsystem.domain.TimeEntry;
import com.timesheetsystem.repository.TimeEntryRepository;
import com.timesheetsystem.service.criteria.TimeEntryCriteria;
import com.timesheetsystem.service.dto.TimeEntryDTO;
import com.timesheetsystem.service.mapper.TimeEntryMapper;
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
 * Service for executing complex queries for {@link TimeEntry} entities in the database.
 * The main input is a {@link TimeEntryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TimeEntryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TimeEntryQueryService extends QueryService<TimeEntry> {

    private static final Logger log = LoggerFactory.getLogger(TimeEntryQueryService.class);

    private final TimeEntryRepository timeEntryRepository;

    private final TimeEntryMapper timeEntryMapper;

    public TimeEntryQueryService(TimeEntryRepository timeEntryRepository, TimeEntryMapper timeEntryMapper) {
        this.timeEntryRepository = timeEntryRepository;
        this.timeEntryMapper = timeEntryMapper;
    }

    /**
     * Return a {@link Page} of {@link TimeEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TimeEntryDTO> findByCriteria(TimeEntryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TimeEntry> specification = createSpecification(criteria);
        return timeEntryRepository.findAll(specification, page).map(timeEntryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TimeEntryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TimeEntry> specification = createSpecification(criteria);
        return timeEntryRepository.count(specification);
    }

    /**
     * Function to convert {@link TimeEntryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TimeEntry> createSpecification(TimeEntryCriteria criteria) {
        Specification<TimeEntry> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TimeEntry_.id));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), TimeEntry_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), TimeEntry_.endTime));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), TimeEntry_.type));
            }
            if (criteria.getOvertimeCategory() != null) {
                specification = specification.and(buildSpecification(criteria.getOvertimeCategory(), TimeEntry_.overtimeCategory));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TimeEntry_.description));
            }
            if (criteria.getHoursWorked() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHoursWorked(), TimeEntry_.hoursWorked));
            }
            if (criteria.getTimesheetId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTimesheetId(), root -> root.join(TimeEntry_.timesheet, JoinType.LEFT).get(Timesheet_.id))
                );
            }
        }
        return specification;
    }
}
