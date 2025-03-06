package com.timesheetsystem.service.mapper;

import com.timesheetsystem.domain.Company;
import com.timesheetsystem.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {}
