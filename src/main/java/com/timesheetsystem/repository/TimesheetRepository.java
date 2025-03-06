package com.timesheetsystem.repository;

import com.timesheetsystem.domain.Timesheet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Timesheet entity.
 */
@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long>, JpaSpecificationExecutor<Timesheet> {
    @Query("select timesheet from Timesheet timesheet where timesheet.user.login = ?#{authentication.name}")
    List<Timesheet> findByUserIsCurrentUser();

    default Optional<Timesheet> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Timesheet> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Timesheet> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select timesheet from Timesheet timesheet left join fetch timesheet.user",
        countQuery = "select count(timesheet) from Timesheet timesheet"
    )
    Page<Timesheet> findAllWithToOneRelationships(Pageable pageable);

    @Query("select timesheet from Timesheet timesheet left join fetch timesheet.user")
    List<Timesheet> findAllWithToOneRelationships();

    @Query("select timesheet from Timesheet timesheet left join fetch timesheet.user where timesheet.id =:id")
    Optional<Timesheet> findOneWithToOneRelationships(@Param("id") Long id);
}
