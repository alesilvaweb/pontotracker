package com.timesheetsystem.service;

import com.timesheetsystem.domain.*; // for static metamodels
import com.timesheetsystem.domain.TimesheetReport;
import com.timesheetsystem.repository.TimesheetReportRepository;
import com.timesheetsystem.service.criteria.TimesheetReportCriteria;
import com.timesheetsystem.service.dto.TimesheetReportDTO;
import com.timesheetsystem.service.mapper.TimesheetReportMapper;
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
 * Service for executing complex queries for {@link TimesheetReport} entities in the database.
 * The main input is a {@link TimesheetReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TimesheetReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TimesheetReportQueryService extends QueryService<TimesheetReport> {

    private static final Logger log = LoggerFactory.getLogger(TimesheetReportQueryService.class);

    private final TimesheetReportRepository timesheetReportRepository;

    private final TimesheetReportMapper timesheetReportMapper;

    public TimesheetReportQueryService(TimesheetReportRepository timesheetReportRepository, TimesheetReportMapper timesheetReportMapper) {
        this.timesheetReportRepository = timesheetReportRepository;
        this.timesheetReportMapper = timesheetReportMapper;
    }

    /**
     * Return a {@link Page} of {@link TimesheetReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TimesheetReportDTO> findByCriteria(TimesheetReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TimesheetReport> specification = createSpecification(criteria);
        return timesheetReportRepository.findAll(specification, page).map(timesheetReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TimesheetReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TimesheetReport> specification = createSpecification(criteria);
        return timesheetReportRepository.count(specification);
    }

    /**
     * Function to convert {@link TimesheetReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TimesheetReport> createSpecification(TimesheetReportCriteria criteria) {
        Specification<TimesheetReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TimesheetReport_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), TimesheetReport_.userId));
            }
            if (criteria.getUserName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUserName(), TimesheetReport_.userName));
            }
            if (criteria.getCompanyId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompanyId(), TimesheetReport_.companyId));
            }
            if (criteria.getCompanyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompanyName(), TimesheetReport_.companyName));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), TimesheetReport_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), TimesheetReport_.endDate));
            }
            if (criteria.getTotalRegularHours() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getTotalRegularHours(), TimesheetReport_.totalRegularHours)
                );
            }
            if (criteria.getTotalOvertimeHours() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getTotalOvertimeHours(), TimesheetReport_.totalOvertimeHours)
                );
            }
            if (criteria.getTotalAllowance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalAllowance(), TimesheetReport_.totalAllowance));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), TimesheetReport_.status));
            }
            if (criteria.getGeneratedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGeneratedAt(), TimesheetReport_.generatedAt));
            }
            if (criteria.getApprovedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApprovedAt(), TimesheetReport_.approvedAt));
            }
            if (criteria.getComments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComments(), TimesheetReport_.comments));
            }
            if (criteria.getGeneratedById() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getGeneratedById(),
                        root -> root.join(TimesheetReport_.generatedBy, JoinType.LEFT).get(User_.id)
                    )
                );
            }
            if (criteria.getApprovedById() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getApprovedById(),
                        root -> root.join(TimesheetReport_.approvedBy, JoinType.LEFT).get(User_.id)
                    )
                );
            }
        }
        return specification;
    }
}
