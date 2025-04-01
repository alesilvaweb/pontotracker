package com.timesheetsystem.repository;

import com.timesheetsystem.domain.TimesheetAudit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TimesheetAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimesheetAuditRepository extends JpaRepository<TimesheetAudit, Long> {}
