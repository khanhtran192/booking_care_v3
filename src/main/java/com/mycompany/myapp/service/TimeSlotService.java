package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.domain.Pack;
import com.mycompany.myapp.domain.TimeSlot;
import com.mycompany.myapp.domain.enumeration.TimeSlotValue;
import com.mycompany.myapp.exception.BadRequestException;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.repository.PackRepository;
import com.mycompany.myapp.repository.TimeSlotRepository;
import com.mycompany.myapp.service.dto.TimeSlotDTO;
import com.mycompany.myapp.service.dto.request.CreateTimeSlotDTO;
import com.mycompany.myapp.service.dto.response.TimeSlotResponseDTO;
import com.mycompany.myapp.service.dto.response.TimeSlotValueResponseDTO;
import com.mycompany.myapp.service.mapper.TimeSlotMapper;
import io.undertow.servlet.core.ApplicationListeners;
import java.time.LocalDate;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
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
    private final CheckUtilService checkUtilService;
    private final DoctorRepository doctorRepository;
    private final PackRepository packRepository;
    private final OrderRepository orderRepository;

    public TimeSlotService(
        TimeSlotRepository timeSlotRepository,
        TimeSlotMapper timeSlotMapper,
        MapperService mapperService,
        CheckUtilService checkUtilService,
        DoctorRepository doctorRepository,
        PackRepository packRepository,
        OrderRepository orderRepository
    ) {
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotMapper = timeSlotMapper;
        this.mapperService = mapperService;
        this.checkUtilService = checkUtilService;
        this.doctorRepository = doctorRepository;
        this.packRepository = packRepository;
        this.orderRepository = orderRepository;
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

    public List<TimeSlotValueResponseDTO> timeSlotValues() {
        return Arrays.stream(TimeSlotValue.values()).map(mapperService::mapToDto).collect(Collectors.toList());
    }

    public void inactiveTimeSlot(Long id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id).orElseThrow(() -> new NotFoundException("TimeSlot: " + id + "not found"));
        timeSlot.setActive(false);
        timeSlotRepository.save(timeSlot);
    }

    public void activeTimeSlot(Long id) {
        TimeSlot timeSlot = timeSlotRepository.findById(id).orElseThrow(() -> new NotFoundException("TimeSlot: " + id + "not found"));
        if (!checkUtilService.checkActiveTimeSlot(timeSlot)) {
            throw new BadRequestException("The time period conflicts with the existing time period.");
        } else {
            timeSlot.setActive(true);
            timeSlotRepository.save(timeSlot);
            log.debug("TimeSlot active successfully");
        }
    }

    public List<TimeSlotResponseDTO> listTimeSlotByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new NotFoundException("Doctor: " + doctorId + "not found"));
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByDoctorAndActiveIsTrue(doctor);
        return timeSlots.stream().map(mapperService::mapToDto).collect(Collectors.toList());
    }

    public List<TimeSlotResponseDTO> allTimeSlotByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new NotFoundException("Doctor: " + doctorId + "not found"));
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByDoctor(doctor);
        return timeSlots.stream().map(mapperService::mapToDto).collect(Collectors.toList());
    }

    public List<TimeSlotResponseDTO> listTimeSlotByPack(Long packId) {
        Pack pack = packRepository.findById(packId).orElseThrow(() -> new NotFoundException("Pack: " + packId + "not found"));
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByPackAndActiveIsTrue(pack);
        return timeSlots.stream().map(mapperService::mapToDto).collect(Collectors.toList());
    }

    public List<TimeSlotResponseDTO> allTimeSlotByPack(Long packId) {
        Pack pack = packRepository.findById(packId).orElseThrow(() -> new NotFoundException("Pack: " + packId + "not found"));
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByPack(pack);
        return timeSlots.stream().map(mapperService::mapToDto).collect(Collectors.toList());
    }

    public List<TimeSlotResponseDTO> listTimeSlotFreeOfDoctor(Long id, LocalDate date) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor: " + id + "not found"));
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByDoctorAndActiveIsTrue(doctor);
        return timeSlots
            .stream()
            .filter(timeSlot -> checkUtilService.checkTimeSlotFree(timeSlot, doctor, date))
            .map(mapperService::mapToDto)
            .collect(Collectors.toList());
    }

    public List<TimeSlot> timeSlotFreeOfDoctor(Long id, LocalDate date) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor: " + id + "not found"));
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByDoctorAndActiveIsTrue(doctor);
        return timeSlots
            .stream()
            .filter(timeSlot -> checkUtilService.checkTimeSlotFree(timeSlot, doctor, date))
            .collect(Collectors.toList());
    }
}
