package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TimeSlot;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.repository.PackRepository;
import com.mycompany.myapp.repository.TimeSlotRepository;
import com.mycompany.myapp.service.dto.TimeSlotDTO;
import com.mycompany.myapp.service.dto.request.CreateTimeSlotDTO;
import com.mycompany.myapp.service.dto.response.TimeSlotResponseDTO;
import com.mycompany.myapp.service.mapper.TimeSlotMapper;
import java.util.Optional;
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

    private final PackRepository packRepository;
    private final DoctorRepository doctorRepository;

    public TimeSlotService(
        TimeSlotRepository timeSlotRepository,
        TimeSlotMapper timeSlotMapper,
        PackRepository packRepository,
        DoctorRepository doctorRepository,
        MapperService mapperService
    ) {
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotMapper = timeSlotMapper;
        this.packRepository = packRepository;
        this.doctorRepository = doctorRepository;
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
        TimeSlot timeSlot = mapToEntity(timeSlotDTO);
        timeSlot = timeSlotRepository.save(timeSlot);
        return mapToDto(timeSlot);
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
        timeSlot = mapToEntity(timeSlotDTO, timeSlot);
        timeSlot = timeSlotRepository.save(timeSlot);
        return mapToDto(timeSlot);
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
        return timeSlotRepository.findAll(pageable).map(this::mapToDto);
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
        return timeSlotRepository.findById(id).map(timeSlot -> mapToDto(timeSlot));
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

    public TimeSlot mapToEntity(CreateTimeSlotDTO timeSlotDTO) {
        if (timeSlotDTO.getPackId() == null && timeSlotDTO.getDoctorId() == null) {
            throw new NotFoundException("Pack and doctor can not be null");
        } else if (timeSlotDTO.getPackId() != null && timeSlotDTO.getDoctorId() != null) {
            throw new RuntimeException("can not has both pack and doctor");
        }
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setTime(timeSlotDTO.getTime());
        timeSlot.setDescription(timeSlotDTO.getDescription());
        timeSlot.setPrice(timeSlotDTO.getPrice());
        timeSlot.setStatus(timeSlotDTO.getStatus());
        if (timeSlotDTO.getPackId() != null && timeSlotDTO.getPackId() != 0) {
            timeSlot.setPack(
                packRepository
                    .findById(timeSlotDTO.getPackId())
                    .orElseThrow(() -> new NotFoundException("Pack: " + timeSlotDTO.getPackId() + "not found"))
            );
        } else if (timeSlotDTO.getDoctorId() != null && timeSlotDTO.getDoctorId() != 0) {
            timeSlot.setDoctor(
                doctorRepository
                    .findById(timeSlotDTO.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Doctor: " + timeSlotDTO.getDoctorId() + "not found"))
            );
        }
        return timeSlot;
    }

    public TimeSlot mapToEntity(CreateTimeSlotDTO timeSlotDTO, TimeSlot timeSlot) {
        if (timeSlotDTO.getPackId() == null && timeSlotDTO.getDoctorId() == null) {
            throw new NotFoundException("Pack and doctor can not be null");
        } else if (timeSlotDTO.getPackId() != null && timeSlotDTO.getDoctorId() != null) {
            throw new RuntimeException("can not has both pack and doctor");
        }
        timeSlot.setTime(timeSlotDTO.getTime());
        timeSlot.setDescription(timeSlotDTO.getDescription());
        timeSlot.setPrice(timeSlotDTO.getPrice());
        timeSlot.setStatus(timeSlotDTO.getStatus());
        if (timeSlotDTO.getPackId() != null && timeSlotDTO.getPackId() != 0) {
            timeSlot.setPack(
                packRepository
                    .findById(timeSlotDTO.getPackId())
                    .orElseThrow(() -> new NotFoundException("Pack: " + timeSlotDTO.getPackId() + "not found"))
            );
        } else if (timeSlotDTO.getDoctorId() != null && timeSlotDTO.getDoctorId() != 0) {
            timeSlot.setDoctor(
                doctorRepository
                    .findById(timeSlotDTO.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Doctor: " + timeSlotDTO.getDoctorId() + "not found"))
            );
        }
        return timeSlot;
    }

    public TimeSlotResponseDTO mapToDto(TimeSlot timeSlot) {
        TimeSlotResponseDTO timeSlotResponseDTO = new TimeSlotResponseDTO();
        timeSlotResponseDTO.setId(timeSlot.getId());
        timeSlotResponseDTO.setDescription(timeSlot.getDescription());
        timeSlotResponseDTO.setStatus(timeSlot.getStatus());
        timeSlotResponseDTO.setPrice(timeSlot.getPrice());
        timeSlotResponseDTO.setPack(mapperService.mapToDto(timeSlot.getPack()));
        timeSlotResponseDTO.setDoctor(mapperService.mapToDto(timeSlot.getDoctor()));
        return timeSlotResponseDTO;
    }
}
