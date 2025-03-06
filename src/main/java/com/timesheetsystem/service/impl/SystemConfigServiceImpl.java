package com.timesheetsystem.service.impl;

import com.timesheetsystem.domain.SystemConfig;
import com.timesheetsystem.repository.SystemConfigRepository;
import com.timesheetsystem.service.SystemConfigService;
import com.timesheetsystem.service.dto.SystemConfigDTO;
import com.timesheetsystem.service.mapper.SystemConfigMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.timesheetsystem.domain.SystemConfig}.
 */
@Service
@Transactional
public class SystemConfigServiceImpl implements SystemConfigService {

    private static final Logger log = LoggerFactory.getLogger(SystemConfigServiceImpl.class);

    private final SystemConfigRepository systemConfigRepository;

    private final SystemConfigMapper systemConfigMapper;

    public SystemConfigServiceImpl(SystemConfigRepository systemConfigRepository, SystemConfigMapper systemConfigMapper) {
        this.systemConfigRepository = systemConfigRepository;
        this.systemConfigMapper = systemConfigMapper;
    }

    @Override
    public SystemConfigDTO save(SystemConfigDTO systemConfigDTO) {
        log.debug("Request to save SystemConfig : {}", systemConfigDTO);
        SystemConfig systemConfig = systemConfigMapper.toEntity(systemConfigDTO);
        systemConfig = systemConfigRepository.save(systemConfig);
        return systemConfigMapper.toDto(systemConfig);
    }

    @Override
    public SystemConfigDTO update(SystemConfigDTO systemConfigDTO) {
        log.debug("Request to update SystemConfig : {}", systemConfigDTO);
        SystemConfig systemConfig = systemConfigMapper.toEntity(systemConfigDTO);
        systemConfig = systemConfigRepository.save(systemConfig);
        return systemConfigMapper.toDto(systemConfig);
    }

    @Override
    public Optional<SystemConfigDTO> partialUpdate(SystemConfigDTO systemConfigDTO) {
        log.debug("Request to partially update SystemConfig : {}", systemConfigDTO);

        return systemConfigRepository
            .findById(systemConfigDTO.getId())
            .map(existingSystemConfig -> {
                systemConfigMapper.partialUpdate(existingSystemConfig, systemConfigDTO);

                return existingSystemConfig;
            })
            .map(systemConfigRepository::save)
            .map(systemConfigMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemConfigDTO> findAll() {
        log.debug("Request to get all SystemConfigs");
        return systemConfigRepository.findAll().stream().map(systemConfigMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemConfigDTO> findOne(Long id) {
        log.debug("Request to get SystemConfig : {}", id);
        return systemConfigRepository.findById(id).map(systemConfigMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SystemConfig : {}", id);
        systemConfigRepository.deleteById(id);
    }
}
