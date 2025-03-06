package com.timesheetsystem.service.mapper;

import com.timesheetsystem.domain.SystemConfig;
import com.timesheetsystem.service.dto.SystemConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemConfig} and its DTO {@link SystemConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface SystemConfigMapper extends EntityMapper<SystemConfigDTO, SystemConfig> {}
