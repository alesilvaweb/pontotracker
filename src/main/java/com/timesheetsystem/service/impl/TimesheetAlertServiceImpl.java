package com.timesheetsystem.service.impl;

import com.timesheetsystem.domain.TimesheetAlert;
import com.timesheetsystem.repository.TimesheetAlertRepository;
import com.timesheetsystem.service.TimesheetAlertService;
import com.timesheetsystem.service.dto.TimesheetAlertDTO;
import com.timesheetsystem.service.mapper.TimesheetAlertMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.timesheetsystem.domain.TimesheetAlert}.
 */
@Service
@Transactional
public class TimesheetAlertServiceImpl implements TimesheetAlertService {

    private static final Logger log = LoggerFactory.getLogger(TimesheetAlertServiceImpl.class);

    private final TimesheetAlertRepository timesheetAlertRepository;

    private final TimesheetAlertMapper timesheetAlertMapper;

    public TimesheetAlertServiceImpl(TimesheetAlertRepository timesheetAlertRepository, TimesheetAlertMapper timesheetAlertMapper) {
        this.timesheetAlertRepository = timesheetAlertRepository;
        this.timesheetAlertMapper = timesheetAlertMapper;
    }

    @Override
    public TimesheetAlertDTO save(TimesheetAlertDTO timesheetAlertDTO) {
        log.debug("Request to save TimesheetAlert : {}", timesheetAlertDTO);
        TimesheetAlert timesheetAlert = timesheetAlertMapper.toEntity(timesheetAlertDTO);
        timesheetAlert = timesheetAlertRepository.save(timesheetAlert);
        return timesheetAlertMapper.toDto(timesheetAlert);
    }

    @Override
    public TimesheetAlertDTO update(TimesheetAlertDTO timesheetAlertDTO) {
        log.debug("Request to update TimesheetAlert : {}", timesheetAlertDTO);
        TimesheetAlert timesheetAlert = timesheetAlertMapper.toEntity(timesheetAlertDTO);
        timesheetAlert = timesheetAlertRepository.save(timesheetAlert);
        return timesheetAlertMapper.toDto(timesheetAlert);
    }

    @Override
    public Optional<TimesheetAlertDTO> partialUpdate(TimesheetAlertDTO timesheetAlertDTO) {
        log.debug("Request to partially update TimesheetAlert : {}", timesheetAlertDTO);

        return timesheetAlertRepository
            .findById(timesheetAlertDTO.getId())
            .map(existingTimesheetAlert -> {
                timesheetAlertMapper.partialUpdate(existingTimesheetAlert, timesheetAlertDTO);

                return existingTimesheetAlert;
            })
            .map(timesheetAlertRepository::save)
            .map(timesheetAlertMapper::toDto);
    }

    public Page<TimesheetAlertDTO> findAllWithEagerRelationships(Pageable pageable) {
        return timesheetAlertRepository.findAllWithEagerRelationships(pageable).map(timesheetAlertMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimesheetAlertDTO> findOne(Long id) {
        log.debug("Request to get TimesheetAlert : {}", id);
        return timesheetAlertRepository.findOneWithEagerRelationships(id).map(timesheetAlertMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TimesheetAlert : {}", id);
        timesheetAlertRepository.deleteById(id);
    }
}
