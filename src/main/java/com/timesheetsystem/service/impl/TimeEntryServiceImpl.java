package com.timesheetsystem.service.impl;

import com.timesheetsystem.domain.TimeEntry;
import com.timesheetsystem.repository.TimeEntryRepository;
import com.timesheetsystem.service.TimeEntryService;
import com.timesheetsystem.service.dto.TimeEntryDTO;
import com.timesheetsystem.service.mapper.TimeEntryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.timesheetsystem.domain.TimeEntry}.
 */
@Service
@Transactional
public class TimeEntryServiceImpl implements TimeEntryService {

    private static final Logger log = LoggerFactory.getLogger(TimeEntryServiceImpl.class);

    private final TimeEntryRepository timeEntryRepository;

    private final TimeEntryMapper timeEntryMapper;

    public TimeEntryServiceImpl(TimeEntryRepository timeEntryRepository, TimeEntryMapper timeEntryMapper) {
        this.timeEntryRepository = timeEntryRepository;
        this.timeEntryMapper = timeEntryMapper;
    }

    @Override
    public TimeEntryDTO save(TimeEntryDTO timeEntryDTO) {
        log.debug("Request to save TimeEntry : {}", timeEntryDTO);
        TimeEntry timeEntry = timeEntryMapper.toEntity(timeEntryDTO);
        timeEntry = timeEntryRepository.save(timeEntry);
        return timeEntryMapper.toDto(timeEntry);
    }

    @Override
    public TimeEntryDTO update(TimeEntryDTO timeEntryDTO) {
        log.debug("Request to update TimeEntry : {}", timeEntryDTO);
        TimeEntry timeEntry = timeEntryMapper.toEntity(timeEntryDTO);
        timeEntry = timeEntryRepository.save(timeEntry);
        return timeEntryMapper.toDto(timeEntry);
    }

    @Override
    public Optional<TimeEntryDTO> partialUpdate(TimeEntryDTO timeEntryDTO) {
        log.debug("Request to partially update TimeEntry : {}", timeEntryDTO);

        return timeEntryRepository
            .findById(timeEntryDTO.getId())
            .map(existingTimeEntry -> {
                timeEntryMapper.partialUpdate(existingTimeEntry, timeEntryDTO);

                return existingTimeEntry;
            })
            .map(timeEntryRepository::save)
            .map(timeEntryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimeEntryDTO> findOne(Long id) {
        log.debug("Request to get TimeEntry : {}", id);
        return timeEntryRepository.findById(id).map(timeEntryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TimeEntry : {}", id);
        timeEntryRepository.deleteById(id);
    }
}
