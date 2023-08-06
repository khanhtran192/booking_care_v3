package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.repository.HospitalRepository;
import com.mycompany.myapp.service.dto.DoctorDTO;
import com.mycompany.myapp.service.dto.request.CreateDoctorDTO;
import com.mycompany.myapp.service.dto.response.DoctorCreatedDTO;
import com.mycompany.myapp.service.dto.response.DoctorResponseDTO;
import com.mycompany.myapp.service.mapper.DepartmentMapper;
import com.mycompany.myapp.service.mapper.DoctorMapper;
import com.mycompany.myapp.service.mapper.HospitalMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Doctor}.
 */
@Service
@Transactional
public class DoctorService {

    private final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    private final DoctorMapper doctorMapper;

    private final MapperService mapperService;

    private final UserService userService;

    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;
    private final DepartmentMapper departmentMapper;

    public DoctorService(
        DoctorRepository doctorRepository,
        DoctorMapper doctorMapper,
        UserService userService,
        HospitalRepository hospitalRepository,
        HospitalMapper hospitalMapper,
        DepartmentMapper departmentMapper,
        MapperService mapperService
    ) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.userService = userService;
        this.hospitalRepository = hospitalRepository;
        this.hospitalMapper = hospitalMapper;
        this.departmentMapper = departmentMapper;
        this.mapperService = mapperService;
    }

    /**
     * Save a doctor.
     *
     * @param doctorDTO the entity to save.
     * @return the persisted entity.
     */
    public DoctorDTO save(DoctorDTO doctorDTO) {
        log.debug("Request to save Doctor : {}", doctorDTO);
        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        doctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(doctor);
    }

    /**
     * Update a doctor.
     *
     * @param doctorDTO the entity to save.
     * @return the persisted entity.
     */
    public DoctorDTO update(DoctorDTO doctorDTO) {
        log.debug("Request to update Doctor : {}", doctorDTO);
        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        doctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(doctor);
    }

    /**
     * Partially update a doctor.
     *
     * @param doctorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DoctorDTO> partialUpdate(DoctorDTO doctorDTO) {
        log.debug("Request to partially update Doctor : {}", doctorDTO);

        return doctorRepository
            .findById(doctorDTO.getId())
            .map(existingDoctor -> {
                doctorMapper.partialUpdate(existingDoctor, doctorDTO);

                return existingDoctor;
            })
            .map(doctorRepository::save)
            .map(doctorMapper::toDto);
    }

    /**
     * Get all the doctors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DoctorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Doctors");
        return doctorRepository.findAll(pageable).map(doctorMapper::toDto);
    }

    /**
     * Get one doctor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DoctorDTO> findOne(Long id) {
        log.debug("Request to get Doctor : {}", id);
        return doctorRepository.findById(id).map(doctorMapper::toDto);
    }

    /**
     * Delete the doctor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Doctor : {}", id);
        doctorRepository.deleteById(id);
    }

    public Page<DoctorDTO> findAllByHospitalId(Pageable pageable, Integer hospitalId, String keyword) {
        return doctorRepository.pageDoctorByHospital(pageable, hospitalId, keyword).map(doctorMapper::toDto);
    }

    public Page<DoctorDTO> findAllByHospitalIdForUser(Pageable pageable, Integer hospitalId, String keyword) {
        return doctorRepository.pageDoctorByHospitalForUser(pageable, hospitalId, keyword).map(doctorMapper::toDto);
    }

    public List<DoctorDTO> findAllByDepartment(Department department) {
        return doctorRepository.findAllByDepartment(department).stream().map(doctorMapper::toDto).collect(Collectors.toList());
    }

    public List<DoctorCreatedDTO> createDoctor(List<CreateDoctorDTO> doctorDTOs) {
        List<Long> emailDoctorCreated = userService.createDoctor(doctorDTOs);
        List<Doctor> doctorCreated = new ArrayList<>();
        for (Long id : emailDoctorCreated) {
            Doctor doctor = doctorRepository.findDoctorByUserId(id);
            doctorCreated.add(doctor);
        }
        return doctorCreated
            .stream()
            .map(doctor -> {
                DoctorCreatedDTO d = new DoctorCreatedDTO();
                d.setName(doctor.getName());
                d.setEmail(doctor.getEmail());
                d.setDepartment(departmentMapper.toDto(doctor.getDepartment()));
                d.setHospital(hospitalMapper.toDto(hospitalRepository.findById(Long.valueOf(doctor.getHospitalId())).orElse(null)));
                return d;
            })
            .collect(Collectors.toList());
    }

    public void inactiveDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new NotFoundException("Doctor not found"));
        userService.deleteDoctor(doctor.getUserId());
        doctor.setActive(false);
        doctorRepository.save(doctor);
        log.debug("delete Doctors: {} success", doctorId);
    }

    public void acticeDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor: " + id + " not found"));
        doctor.setActive(true);
        doctorRepository.save(doctor);
        userService.activeDoctor(id);
    }

    public List<DoctorResponseDTO> listDoctorMostRate() {
        List<Doctor> doctors = doctorRepository
            .findAll()
            .stream()
            .filter(doctor -> doctor.getActive() == true)
            .sorted((Comparator.comparing(doctor -> doctor.getRate())))
            .limit(5)
            .collect(Collectors.toList());
        return doctors.stream().map(mapperService::mapToDto).collect(Collectors.toList());
    }
}
