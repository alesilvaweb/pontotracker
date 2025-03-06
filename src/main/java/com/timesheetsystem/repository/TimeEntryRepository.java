package com.timesheetsystem.repository;

import com.timesheetsystem.domain.TimeEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TimeEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, Long>, JpaSpecificationExecutor<TimeEntry> {}
