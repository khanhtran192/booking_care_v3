package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.repository.HospitalRepository;
import com.mycompany.myapp.service.dto.DoctorDTO;
import com.mycompany.myapp.service.dto.request.CreateDoctorDTO;
import com.mycompany.myapp.service.dto.response.DoctorCreatedDTO;
import com.mycompany.myapp.service.mapper.DepartmentMapper;
import com.mycompany.myapp.service.mapper.DoctorMapper;
import com.mycompany.myapp.service.mapper.HospitalMapper;
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
 * Service Implementation for managing {@link Doctor}.
 */
@Service
@Transactional
public class DoctorService {

    private final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    private final DoctorMapper doctorMapper;

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
        DepartmentMapper departmentMapper
    ) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.userService = userService;
        this.hospitalRepository = hospitalRepository;
        this.hospitalMapper = hospitalMapper;
        this.departmentMapper = departmentMapper;
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

    public List<DoctorDTO> findAllByHospitalId(Integer hospitalId) {
        return doctorRepository.findAllByHospitalId(hospitalId).stream().map(doctorMapper::toDto).collect(Collectors.toList());
    }

    public List<DoctorDTO> findAllByDepartment(Department department) {
        return doctorRepository.findAllByDepartment(department).stream().map(doctorMapper::toDto).collect(Collectors.toList());
    }

    public List<DoctorCreatedDTO> createDoctor(List<CreateDoctorDTO> doctorDTOs) {
        List<String> emailDoctorCreated = userService.createDoctor(doctorDTOs);
        List<Doctor> doctorCreated = emailDoctorCreated.stream().distinct().map(doctorRepository::findByEmail).collect(Collectors.toList());
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
}
