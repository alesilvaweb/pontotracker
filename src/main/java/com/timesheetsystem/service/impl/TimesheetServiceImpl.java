package com.timesheetsystem.service.impl;

import com.timesheetsystem.domain.Timesheet;
import com.timesheetsystem.repository.TimesheetRepository;
import com.timesheetsystem.service.TimesheetService;
import com.timesheetsystem.service.dto.TimesheetDTO;
import com.timesheetsystem.service.mapper.TimesheetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.timesheetsystem.domain.Timesheet}.
 */
@Service
@Transactional
public class TimesheetServiceImpl implements TimesheetService {

    private static final Logger log = LoggerFactory.getLogger(TimesheetServiceImpl.class);

    private final TimesheetRepository timesheetRepository;

    private final TimesheetMapper timesheetMapper;

    public TimesheetServiceImpl(TimesheetRepository timesheetRepository, TimesheetMapper timesheetMapper) {
        this.timesheetRepository = timesheetRepository;
        this.timesheetMapper = timesheetMapper;
    }

    @Override
    public TimesheetDTO save(TimesheetDTO timesheetDTO) {
        log.debug("Request to save Timesheet : {}", timesheetDTO);
        Timesheet timesheet = timesheetMapper.toEntity(timesheetDTO);
        timesheet = timesheetRepository.save(timesheet);
        return timesheetMapper.toDto(timesheet);
    }

    @Override
    public TimesheetDTO update(TimesheetDTO timesheetDTO) {
        log.debug("Request to update Timesheet : {}", timesheetDTO);
        Timesheet timesheet = timesheetMapper.toEntity(timesheetDTO);
        timesheet = timesheetRepository.save(timesheet);
        return timesheetMapper.toDto(timesheet);
    }

    @Override
    public Optional<TimesheetDTO> partialUpdate(TimesheetDTO timesheetDTO) {
        log.debug("Request to partially update Timesheet : {}", timesheetDTO);

        return timesheetRepository
            .findById(timesheetDTO.getId())
            .map(existingTimesheet -> {
                timesheetMapper.partialUpdate(existingTimesheet, timesheetDTO);

                return existingTimesheet;
            })
            .map(timesheetRepository::save)
            .map(timesheetMapper::toDto);
    }

    public Page<TimesheetDTO> findAllWithEagerRelationships(Pageable pageable) {
        return timesheetRepository.findAllWithEagerRelationships(pageable).map(timesheetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimesheetDTO> findOne(Long id) {
        log.debug("Request to get Timesheet : {}", id);
        return timesheetRepository.findOneWithEagerRelationships(id).map(timesheetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Timesheet : {}", id);
        timesheetRepository.deleteById(id);
    }
}
