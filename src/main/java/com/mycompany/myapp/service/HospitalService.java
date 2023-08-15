package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Hospital;
import com.mycompany.myapp.domain.Pack;
import com.mycompany.myapp.domain.enumeration.FacilityType;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.HospitalRepository;
import com.mycompany.myapp.service.dto.DepartmentDTO;
import com.mycompany.myapp.service.dto.DoctorDTO;
import com.mycompany.myapp.service.dto.HospitalDTO;
import com.mycompany.myapp.service.dto.response.DepartmentResponseDTO;
import com.mycompany.myapp.service.dto.response.DoctorResponseDTO;
import com.mycompany.myapp.service.dto.response.HospitalInfoResponseDTO;
import com.mycompany.myapp.service.mapper.HospitalMapper;
import java.util.List;
import java.util.Optional;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Hospital}.
 */
@Service
@Transactional
public class HospitalService {

    private final Logger log = LoggerFactory.getLogger(HospitalService.class);

    private final HospitalRepository hospitalRepository;

    private final HospitalMapper hospitalMapper;

    private final DoctorService doctorService;

    private final UserService userService;

    private final DepartmentService departmentService;

    public HospitalService(
        HospitalRepository hospitalRepository,
        HospitalMapper hospitalMapper,
        DoctorService doctorService,
        DepartmentService departmentService,
        UserService userService
    ) {
        this.hospitalRepository = hospitalRepository;
        this.hospitalMapper = hospitalMapper;
        this.doctorService = doctorService;
        this.departmentService = departmentService;
        this.userService = userService;
    }

    /**
     * Save a hospital.
     *
     * @param hospitalDTO the entity to save.
     * @return the persisted entity.
     */
    public HospitalDTO save(HospitalDTO hospitalDTO) {
        log.debug("Request to save Hospital : {}", hospitalDTO);
        Hospital hospital = hospitalMapper.toEntity(hospitalDTO);
        hospital = hospitalRepository.save(hospital);
        return hospitalMapper.toDto(hospital);
    }

    /**
     * Update a hospital.
     *
     * @param hospitalDTO the entity to save.
     * @return the persisted entity.
     */
    public HospitalDTO update(HospitalDTO hospitalDTO) {
        log.debug("Request to update Hospital : {}", hospitalDTO);
        Hospital hospital = hospitalMapper.toEntity(hospitalDTO);
        hospital = hospitalRepository.save(hospital);
        return hospitalMapper.toDto(hospital);
    }

    /**
     * Get all the hospitals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HospitalDTO> findAllForUser(Pageable pageable, String keyword) {
        log.debug("Request to get all Hospitals");
        return hospitalRepository.listHospitalForUser(pageable, keyword).map(hospitalMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<HospitalDTO> findAll(Pageable pageable, String keyword) {
        log.debug("Request to get all Hospitals");
        return hospitalRepository.listHospital(pageable, keyword).map(hospitalMapper::toDto);
    }

    /**
     * Get one hospital by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HospitalDTO> findOne(Long id) {
        log.debug("Request to get Hospital : {}", id);
        return hospitalRepository.findById(id).map(hospitalMapper::toDto);
    }

    /**
     * Delete the hospital by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to inactive Hospital : {}", id);
        Hospital hospital = hospitalRepository.findById(id).orElseThrow(() -> new NotFoundException("Hospital: " + id + " not found"));
        hospital.setActive(false);
        hospitalRepository.save(hospital);
        userService.deleteHospital(hospital.getUserId());
    }

    public Page<DoctorResponseDTO> getAllDoctor(Pageable pageable, Integer id, String keyword) {
        return doctorService.findAllByHospitalId(pageable, id, keyword);
    }

    public Page<DoctorResponseDTO> getAllDoctorForUser(Pageable pageable, Integer id, String keyword) {
        return doctorService.findAllByHospitalIdForUser(pageable, id, keyword);
    }

    public Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable, Long id, String keyword) {
        Hospital hospital = hospitalRepository.findById(id).orElseThrow(() -> new RuntimeException("Hospital not found"));
        return departmentService.findAllByHospital(pageable, hospital, keyword);
    }

    public Page<DepartmentResponseDTO> getAllDepartmentsForUser(Pageable pageable, Long id, String keyword) {
        Hospital hospital = hospitalRepository.findById(id).orElseThrow(() -> new RuntimeException("Hospital not found"));
        return departmentService.findAllByHospitalForUser(pageable, hospital, keyword);
    }

    public void activeHospital(Long id) {
        Hospital hospital = hospitalRepository.findById(id).orElseThrow(() -> new NotFoundException("Hospital: " + id + " not found"));
        hospital.setActive(true);
        hospitalRepository.save(hospital);
        userService.activeHospital(id);
    }

    public List<FacilityType> listFacilities() {
        return List.of(FacilityType.values());
    }
}
