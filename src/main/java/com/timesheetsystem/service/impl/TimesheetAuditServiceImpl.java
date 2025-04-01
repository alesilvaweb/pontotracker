package com.timesheetsystem.service.impl;

import com.timesheetsystem.domain.TimesheetAudit;
import com.timesheetsystem.repository.TimesheetAuditRepository;
import com.timesheetsystem.service.TimesheetAuditService;
import com.timesheetsystem.service.dto.TimesheetAuditDTO;
import com.timesheetsystem.service.mapper.TimesheetAuditMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.timesheetsystem.domain.TimesheetAudit}.
 */
@Service
@Transactional
public class TimesheetAuditServiceImpl implements TimesheetAuditService {

    private static final Logger log = LoggerFactory.getLogger(TimesheetAuditServiceImpl.class);

    private final TimesheetAuditRepository timesheetAuditRepository;

    private final TimesheetAuditMapper timesheetAuditMapper;

    public TimesheetAuditServiceImpl(TimesheetAuditRepository timesheetAuditRepository, TimesheetAuditMapper timesheetAuditMapper) {
        this.timesheetAuditRepository = timesheetAuditRepository;
        this.timesheetAuditMapper = timesheetAuditMapper;
    }

    @Override
    public TimesheetAuditDTO save(TimesheetAuditDTO timesheetAuditDTO) {
        log.debug("Request to save TimesheetAudit : {}", timesheetAuditDTO);
        TimesheetAudit timesheetAudit = timesheetAuditMapper.toEntity(timesheetAuditDTO);
        timesheetAudit = timesheetAuditRepository.save(timesheetAudit);
        return timesheetAuditMapper.toDto(timesheetAudit);
    }

    @Override
    public TimesheetAuditDTO update(TimesheetAuditDTO timesheetAuditDTO) {
        log.debug("Request to update TimesheetAudit : {}", timesheetAuditDTO);
        TimesheetAudit timesheetAudit = timesheetAuditMapper.toEntity(timesheetAuditDTO);
        timesheetAudit = timesheetAuditRepository.save(timesheetAudit);
        return timesheetAuditMapper.toDto(timesheetAudit);
    }

    @Override
    public Optional<TimesheetAuditDTO> partialUpdate(TimesheetAuditDTO timesheetAuditDTO) {
        log.debug("Request to partially update TimesheetAudit : {}", timesheetAuditDTO);

        return timesheetAuditRepository
            .findById(timesheetAuditDTO.getId())
            .map(existingTimesheetAudit -> {
                timesheetAuditMapper.partialUpdate(existingTimesheetAudit, timesheetAuditDTO);

                return existingTimesheetAudit;
            })
            .map(timesheetAuditRepository::save)
            .map(timesheetAuditMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimesheetAuditDTO> findAll() {
        log.debug("Request to get all TimesheetAudits");
        return timesheetAuditRepository
            .findAll()
            .stream()
            .map(timesheetAuditMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimesheetAuditDTO> findOne(Long id) {
        log.debug("Request to get TimesheetAudit : {}", id);
        return timesheetAuditRepository.findById(id).map(timesheetAuditMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TimesheetAudit : {}", id);
        timesheetAuditRepository.deleteById(id);
    }
}
