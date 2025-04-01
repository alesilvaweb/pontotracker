package com.timesheetsystem.repository;

import com.timesheetsystem.domain.CompanyAllowance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CompanyAllowance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyAllowanceRepository extends JpaRepository<CompanyAllowance, Long> {}
