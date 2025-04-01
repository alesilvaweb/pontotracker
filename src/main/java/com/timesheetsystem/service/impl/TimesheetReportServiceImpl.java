package com.timesheetsystem.service.impl;

import com.timesheetsystem.domain.TimesheetReport;
import com.timesheetsystem.repository.TimesheetReportRepository;
import com.timesheetsystem.service.TimesheetReportService;
import com.timesheetsystem.service.dto.TimesheetReportDTO;
import com.timesheetsystem.service.mapper.TimesheetReportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.timesheetsystem.domain.TimesheetReport}.
 */
@Service
@Transactional
public class TimesheetReportServiceImpl implements TimesheetReportService {

    private static final Logger log = LoggerFactory.getLogger(TimesheetReportServiceImpl.class);

    private final TimesheetReportRepository timesheetReportRepository;

    private final TimesheetReportMapper timesheetReportMapper;

    public TimesheetReportServiceImpl(TimesheetReportRepository timesheetReportRepository, TimesheetReportMapper timesheetReportMapper) {
        this.timesheetReportRepository = timesheetReportRepository;
        this.timesheetReportMapper = timesheetReportMapper;
    }

    @Override
    public TimesheetReportDTO save(TimesheetReportDTO timesheetReportDTO) {
        log.debug("Request to save TimesheetReport : {}", timesheetReportDTO);
        TimesheetReport timesheetReport = timesheetReportMapper.toEntity(timesheetReportDTO);
        timesheetReport = timesheetReportRepository.save(timesheetReport);
        return timesheetReportMapper.toDto(timesheetReport);
    }

    @Override
    public TimesheetReportDTO update(TimesheetReportDTO timesheetReportDTO) {
        log.debug("Request to update TimesheetReport : {}", timesheetReportDTO);
        TimesheetReport timesheetReport = timesheetReportMapper.toEntity(timesheetReportDTO);
        timesheetReport = timesheetReportRepository.save(timesheetReport);
        return timesheetReportMapper.toDto(timesheetReport);
    }

    @Override
    public Optional<TimesheetReportDTO> partialUpdate(TimesheetReportDTO timesheetReportDTO) {
        log.debug("Request to partially update TimesheetReport : {}", timesheetReportDTO);

        return timesheetReportRepository
            .findById(timesheetReportDTO.getId())
            .map(existingTimesheetReport -> {
                timesheetReportMapper.partialUpdate(existingTimesheetReport, timesheetReportDTO);

                return existingTimesheetReport;
            })
            .map(timesheetReportRepository::save)
            .map(timesheetReportMapper::toDto);
    }

    public Page<TimesheetReportDTO> findAllWithEagerRelationships(Pageable pageable) {
        return timesheetReportRepository.findAllWithEagerRelationships(pageable).map(timesheetReportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimesheetReportDTO> findOne(Long id) {
        log.debug("Request to get TimesheetReport : {}", id);
        return timesheetReportRepository.findOneWithEagerRelationships(id).map(timesheetReportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TimesheetReport : {}", id);
        timesheetReportRepository.deleteById(id);
    }
}
