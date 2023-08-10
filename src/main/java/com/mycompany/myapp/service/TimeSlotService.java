package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TimeSlot;
import com.mycompany.myapp.domain.enumeration.TimeSlotValue;
import com.mycompany.myapp.exception.BadRequestException;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.repository.PackRepository;
import com.mycompany.myapp.repository.TimeSlotRepository;
import com.mycompany.myapp.service.dto.TimeSlotDTO;
import com.mycompany.myapp.service.dto.request.CreateTimeSlotDTO;
import com.mycompany.myapp.service.dto.response.TimeSlotResponseDTO;
import com.mycompany.myapp.service.dto.response.TimeSlotValueResponseDTO;
import com.mycompany.myapp.service.mapper.TimeSlotMapper;
import java.util.Arrays;
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
 * Service Implementation for managing {@link TimeSlot}.
 */
@Service
@Transactional
public class TimeSlotService {

    private final Logger log = LoggerFactory.getLogger(TimeSlotService.class);

    private final TimeSlotRepository timeSlotRepository;

    private final TimeSlotMapper timeSlotMapper;

    private final MapperService mapperService;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, TimeSlotMapper timeSlotMapper, MapperService mapperService) {
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotMapper = timeSlotMapper;
        this.mapperService = mapperService;
    }

    /**
     * Save a timeSlot.
     *
     * @param timeSlotDTO the entity to save.
     * @return the persisted entity.
     */
    public TimeSlotResponseDTO save(CreateTimeSlotDTO timeSlotDTO) {
        log.debug("Request to save TimeSlot : {}", timeSlotDTO);
        TimeSlot timeSlot = mapperService.mapToEntity(timeSlotDTO);
        timeSlot = timeSlotRepository.save(timeSlot);
        return mapperService.mapToDto(timeSlot);
    }

    /**
     * Update a timeSlot.
     *
     * @param timeSlotDTO the entity to save.
     * @return the persisted entity.
     */
    public TimeSlotResponseDTO update(CreateTimeSlotDTO timeSlotDTO, Long id) {
        log.debug("Request to update TimeSlot : {}", timeSlotDTO);
        TimeSlot timeSlot = timeSlotRepository.findById(id).orElseThrow(() -> new NotFoundException("TimeSlot: " + id + "not found"));
        timeSlot = mapperService.mapToEntity(timeSlotDTO, timeSlot);
        timeSlot = timeSlotRepository.save(timeSlot);
        return mapperService.mapToDto(timeSlot);
    }

    /**
     * Partially update a timeSlot.
     *
     * @param timeSlotDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TimeSlotDTO> partialUpdate(TimeSlotDTO timeSlotDTO) {
        log.debug("Request to partially update TimeSlot : {}", timeSlotDTO);

        return timeSlotRepository
            .findById(timeSlotDTO.getId())
            .map(existingTimeSlot -> {
                timeSlotMapper.partialUpdate(existingTimeSlot, timeSlotDTO);

                return existingTimeSlot;
            })
            .map(timeSlotRepository::save)
            .map(timeSlotMapper::toDto);
    }

    /**
     * Get all the timeSlots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TimeSlotResponseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TimeSlots");
        return timeSlotRepository.findAll(pageable).map(mapperService::mapToDto);
    }

    /**
     * Get one timeSlot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TimeSlotResponseDTO> findOne(Long id) {
        log.debug("Request to get TimeSlot : {}", id);
        return timeSlotRepository.findById(id).map(mapperService::mapToDto);
    }

    /**
     * Delete the timeSlot by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TimeSlot : {}", id);
        timeSlotRepository.deleteById(id);
    }

    public List<TimeSlotValueResponseDTO> timeSlotValues() {
        return Arrays.stream(TimeSlotValue.values()).map(mapperService::mapToDto).collect(Collectors.toList());
    }
}
