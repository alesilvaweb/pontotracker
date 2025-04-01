package com.timesheetsystem.service.impl;

import com.timesheetsystem.domain.CompanyAllowance;
import com.timesheetsystem.repository.CompanyAllowanceRepository;
import com.timesheetsystem.service.CompanyAllowanceService;
import com.timesheetsystem.service.dto.CompanyAllowanceDTO;
import com.timesheetsystem.service.mapper.CompanyAllowanceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.timesheetsystem.domain.CompanyAllowance}.
 */
@Service
@Transactional
public class CompanyAllowanceServiceImpl implements CompanyAllowanceService {

    private static final Logger log = LoggerFactory.getLogger(CompanyAllowanceServiceImpl.class);

    private final CompanyAllowanceRepository companyAllowanceRepository;

    private final CompanyAllowanceMapper companyAllowanceMapper;

    public CompanyAllowanceServiceImpl(
        CompanyAllowanceRepository companyAllowanceRepository,
        CompanyAllowanceMapper companyAllowanceMapper
    ) {
        this.companyAllowanceRepository = companyAllowanceRepository;
        this.companyAllowanceMapper = companyAllowanceMapper;
    }

    @Override
    public CompanyAllowanceDTO save(CompanyAllowanceDTO companyAllowanceDTO) {
        log.debug("Request to save CompanyAllowance : {}", companyAllowanceDTO);
        CompanyAllowance companyAllowance = companyAllowanceMapper.toEntity(companyAllowanceDTO);
        companyAllowance = companyAllowanceRepository.save(companyAllowance);
        return companyAllowanceMapper.toDto(companyAllowance);
    }

    @Override
    public CompanyAllowanceDTO update(CompanyAllowanceDTO companyAllowanceDTO) {
        log.debug("Request to update CompanyAllowance : {}", companyAllowanceDTO);
        CompanyAllowance companyAllowance = companyAllowanceMapper.toEntity(companyAllowanceDTO);
        companyAllowance = companyAllowanceRepository.save(companyAllowance);
        return companyAllowanceMapper.toDto(companyAllowance);
    }

    @Override
    public Optional<CompanyAllowanceDTO> partialUpdate(CompanyAllowanceDTO companyAllowanceDTO) {
        log.debug("Request to partially update CompanyAllowance : {}", companyAllowanceDTO);

        return companyAllowanceRepository
            .findById(companyAllowanceDTO.getId())
            .map(existingCompanyAllowance -> {
                companyAllowanceMapper.partialUpdate(existingCompanyAllowance, companyAllowanceDTO);

                return existingCompanyAllowance;
            })
            .map(companyAllowanceRepository::save)
            .map(companyAllowanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyAllowanceDTO> findAll() {
        log.debug("Request to get all CompanyAllowances");
        return companyAllowanceRepository
            .findAll()
            .stream()
            .map(companyAllowanceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompanyAllowanceDTO> findOne(Long id) {
        log.debug("Request to get CompanyAllowance : {}", id);
        return companyAllowanceRepository.findById(id).map(companyAllowanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompanyAllowance : {}", id);
        companyAllowanceRepository.deleteById(id);
    }
}
