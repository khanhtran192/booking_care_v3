package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.domain.Hospital;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.DepartmentRepository;
import com.mycompany.myapp.service.dto.DepartmentDTO;
import com.mycompany.myapp.service.dto.DoctorDTO;
import com.mycompany.myapp.service.dto.response.DepartmentResponseDTO;
import com.mycompany.myapp.service.dto.response.DoctorResponseDTO;
import com.mycompany.myapp.service.mapper.DepartmentMapper;
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
 * Service Implementation for managing {@link Department}.
 */
@Service
@Transactional
public class DepartmentService {

    private final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;

    private final DepartmentMapper departmentMapper;
    private final DoctorService doctorService;

    private final MapperService mapperService;

    public DepartmentService(
        DepartmentRepository departmentRepository,
        DepartmentMapper departmentMapper,
        DoctorService doctorService,
        MapperService mapperService
    ) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.doctorService = doctorService;
        this.mapperService = mapperService;
    }

    /**
     * Save a department.
     *
     * @param departmentDTO the entity to save.
     * @return the persisted entity.
     */
    public DepartmentResponseDTO save(DepartmentDTO departmentDTO) {
        log.debug("Request to save Department : {}", departmentDTO);
        Department department = departmentMapper.toEntity(departmentDTO);
        department = departmentRepository.save(department);
        return mapperService.mapToDto(department);
    }

    /**
     * Update a department.
     *
     * @param departmentDTO the entity to save.
     * @return the persisted entity.
     */
    public DepartmentResponseDTO update(DepartmentDTO departmentDTO) {
        log.debug("Request to update Department : {}", departmentDTO);
        Department department = departmentMapper.toEntity(departmentDTO);
        department = departmentRepository.save(department);
        return mapperService.mapToDto(department);
    }

    /**
     * Partially update a department.
     *
     * @param departmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DepartmentDTO> partialUpdate(DepartmentDTO departmentDTO) {
        log.debug("Request to partially update Department : {}", departmentDTO);

        return departmentRepository
            .findById(departmentDTO.getId())
            .map(existingDepartment -> {
                departmentMapper.partialUpdate(existingDepartment, departmentDTO);

                return existingDepartment;
            })
            .map(departmentRepository::save)
            .map(departmentMapper::toDto);
    }

    /**
     * Get all the departments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DepartmentResponseDTO> findAll(Pageable pageable, String keyword) {
        log.debug("Request to get all Departments");
        return departmentRepository.pageDepartmentForUser(pageable, keyword).map(mapperService::mapToDto);
    }

    /**
     * Get one department by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepartmentResponseDTO> findOne(Long id) {
        log.debug("Request to get Department : {}", id);
        return departmentRepository.findById(id).map(mapperService::mapToDto);
    }

    /**
     * Delete the department by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Department : {}", id);
        departmentRepository.deleteById(id);
    }

    public List<DoctorResponseDTO> findAllByDepartment(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("No department found"));
        return doctorService.findAllByDepartment(department);
    }

    public Page<DepartmentResponseDTO> findAllByHospital(Pageable pageable, Hospital hospital, String keyword) {
        return departmentRepository.pageDepartmentByHospital(pageable, hospital, keyword).map(mapperService::mapToDto);
    }

    public Page<DepartmentResponseDTO> findAllByHospitalForUser(Pageable pageable, Hospital hospital, String keyword) {
        return departmentRepository.pageDepartmentByHospitalForUser(pageable, hospital, keyword).map(mapperService::mapToDto);
    }

    public void inactiveDepartment(Long id) {
        Department department = departmentRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Department: " + id + " not found"));
        department.setActive(false);
        departmentRepository.save(department);
    }

    public void activeDepartment(Long id) {
        Department department = departmentRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Department: " + id + " not found"));
        department.setActive(true);
        departmentRepository.save(department);
    }
}
