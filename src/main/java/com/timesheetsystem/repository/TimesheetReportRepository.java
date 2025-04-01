package com.timesheetsystem.repository;

import com.timesheetsystem.domain.TimesheetReport;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TimesheetReport entity.
 */
@Repository
public interface TimesheetReportRepository extends JpaRepository<TimesheetReport, Long>, JpaSpecificationExecutor<TimesheetReport> {
    @Query("select timesheetReport from TimesheetReport timesheetReport where timesheetReport.generatedBy.login = ?#{authentication.name}")
    List<TimesheetReport> findByGeneratedByIsCurrentUser();

    @Query("select timesheetReport from TimesheetReport timesheetReport where timesheetReport.approvedBy.login = ?#{authentication.name}")
    List<TimesheetReport> findByApprovedByIsCurrentUser();

    default Optional<TimesheetReport> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TimesheetReport> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TimesheetReport> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select timesheetReport from TimesheetReport timesheetReport left join fetch timesheetReport.generatedBy left join fetch timesheetReport.approvedBy",
        countQuery = "select count(timesheetReport) from TimesheetReport timesheetReport"
    )
    Page<TimesheetReport> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select timesheetReport from TimesheetReport timesheetReport left join fetch timesheetReport.generatedBy left join fetch timesheetReport.approvedBy"
    )
    List<TimesheetReport> findAllWithToOneRelationships();

    @Query(
        "select timesheetReport from TimesheetReport timesheetReport left join fetch timesheetReport.generatedBy left join fetch timesheetReport.approvedBy where timesheetReport.id =:id"
    )
    Optional<TimesheetReport> findOneWithToOneRelationships(@Param("id") Long id);
}
