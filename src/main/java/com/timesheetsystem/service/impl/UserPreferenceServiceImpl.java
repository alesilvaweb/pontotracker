package com.timesheetsystem.service.impl;

import com.timesheetsystem.domain.UserPreference;
import com.timesheetsystem.repository.UserPreferenceRepository;
import com.timesheetsystem.service.UserPreferenceService;
import com.timesheetsystem.service.dto.UserPreferenceDTO;
import com.timesheetsystem.service.mapper.UserPreferenceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.timesheetsystem.domain.UserPreference}.
 */
@Service
@Transactional
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private static final Logger log = LoggerFactory.getLogger(UserPreferenceServiceImpl.class);

    private final UserPreferenceRepository userPreferenceRepository;

    private final UserPreferenceMapper userPreferenceMapper;

    public UserPreferenceServiceImpl(UserPreferenceRepository userPreferenceRepository, UserPreferenceMapper userPreferenceMapper) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.userPreferenceMapper = userPreferenceMapper;
    }

    @Override
    public UserPreferenceDTO save(UserPreferenceDTO userPreferenceDTO) {
        log.debug("Request to save UserPreference : {}", userPreferenceDTO);
        UserPreference userPreference = userPreferenceMapper.toEntity(userPreferenceDTO);
        userPreference = userPreferenceRepository.save(userPreference);
        return userPreferenceMapper.toDto(userPreference);
    }

    @Override
    public UserPreferenceDTO update(UserPreferenceDTO userPreferenceDTO) {
        log.debug("Request to update UserPreference : {}", userPreferenceDTO);
        UserPreference userPreference = userPreferenceMapper.toEntity(userPreferenceDTO);
        userPreference = userPreferenceRepository.save(userPreference);
        return userPreferenceMapper.toDto(userPreference);
    }

    @Override
    public Optional<UserPreferenceDTO> partialUpdate(UserPreferenceDTO userPreferenceDTO) {
        log.debug("Request to partially update UserPreference : {}", userPreferenceDTO);

        return userPreferenceRepository
            .findById(userPreferenceDTO.getId())
            .map(existingUserPreference -> {
                userPreferenceMapper.partialUpdate(existingUserPreference, userPreferenceDTO);

                return existingUserPreference;
            })
            .map(userPreferenceRepository::save)
            .map(userPreferenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserPreferenceDTO> findAll() {
        log.debug("Request to get all UserPreferences");
        return userPreferenceRepository
            .findAll()
            .stream()
            .map(userPreferenceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<UserPreferenceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userPreferenceRepository.findAllWithEagerRelationships(pageable).map(userPreferenceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserPreferenceDTO> findOne(Long id) {
        log.debug("Request to get UserPreference : {}", id);
        return userPreferenceRepository.findOneWithEagerRelationships(id).map(userPreferenceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserPreference : {}", id);
        userPreferenceRepository.deleteById(id);
    }
}
