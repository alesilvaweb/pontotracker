package com.timesheetsystem.service.mapper;

import com.timesheetsystem.domain.Company;
import com.timesheetsystem.domain.CompanyAllowance;
import com.timesheetsystem.service.dto.CompanyAllowanceDTO;
import com.timesheetsystem.service.dto.CompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompanyAllowance} and its DTO {@link CompanyAllowanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompanyAllowanceMapper extends EntityMapper<CompanyAllowanceDTO, CompanyAllowance> {
    @Mapping(target = "company", source = "company", qualifiedByName = "companyId")
    CompanyAllowanceDTO toDto(CompanyAllowance s);

    @Named("companyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyDTO toDtoCompanyId(Company company);
}
