package com.timesheetsystem.repository;

import com.timesheetsystem.domain.TimesheetAlert;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TimesheetAlert entity.
 */
@Repository
public interface TimesheetAlertRepository extends JpaRepository<TimesheetAlert, Long>, JpaSpecificationExecutor<TimesheetAlert> {
    @Query("select timesheetAlert from TimesheetAlert timesheetAlert where timesheetAlert.resolvedBy.login = ?#{authentication.name}")
    List<TimesheetAlert> findByResolvedByIsCurrentUser();

    default Optional<TimesheetAlert> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TimesheetAlert> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TimesheetAlert> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select timesheetAlert from TimesheetAlert timesheetAlert left join fetch timesheetAlert.resolvedBy",
        countQuery = "select count(timesheetAlert) from TimesheetAlert timesheetAlert"
    )
    Page<TimesheetAlert> findAllWithToOneRelationships(Pageable pageable);

    @Query("select timesheetAlert from TimesheetAlert timesheetAlert left join fetch timesheetAlert.resolvedBy")
    List<TimesheetAlert> findAllWithToOneRelationships();

    @Query(
        "select timesheetAlert from TimesheetAlert timesheetAlert left join fetch timesheetAlert.resolvedBy where timesheetAlert.id =:id"
    )
    Optional<TimesheetAlert> findOneWithToOneRelationships(@Param("id") Long id);
}
