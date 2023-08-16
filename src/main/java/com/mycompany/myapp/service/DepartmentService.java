package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.domain.Hospital;
import com.mycompany.myapp.domain.Image;
import com.mycompany.myapp.domain.enumeration.ImageType;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.DepartmentRepository;
import com.mycompany.myapp.repository.ImageRepository;
import com.mycompany.myapp.service.dto.DepartmentDTO;
import com.mycompany.myapp.service.dto.FileDTO;
import com.mycompany.myapp.service.dto.response.DepartmentResponseDTO;
import com.mycompany.myapp.service.dto.response.DoctorResponseDTO;
import com.mycompany.myapp.service.mapper.DepartmentMapper;
import java.util.List;
import java.util.Optional;
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
    private final MinioService minioService;
    private final ImageRepository imageRepository;

    public DepartmentService(
        DepartmentRepository departmentRepository,
        DepartmentMapper departmentMapper,
        DoctorService doctorService,
        MapperService mapperService,
        MinioService minioService,
        ImageRepository imageRepository
    ) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.doctorService = doctorService;
        this.mapperService = mapperService;
        this.minioService = minioService;
        this.imageRepository = imageRepository;
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

    public void uploadLogo(Long id, FileDTO fileDTO) {
        Department department = departmentRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Department: " + id + " not found"));
        String name = minioService.uploadFile(fileDTO);
        String path = minioService.getObject(name);
        Image image = new Image();
        image.setName(name);
        image.setPath(path);
        image.setDepartmentId(department.getId());
        image.setType(ImageType.LOGO);
        Image old = imageRepository.findByDepartmentIdAndType(id, ImageType.LOGO);
        if (old != null) {
            imageRepository.delete(old);
        }
        imageRepository.save(image);
    }

    public void uploadBackground(Long id, FileDTO file) {
        Department department = departmentRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Department: " + id + " not found"));
        String name = minioService.uploadFile(file);
        String path = minioService.getObject(name);
        Image image = new Image();
        image.setName(name);
        image.setPath(path);
        image.setDepartmentId(department.getId());
        image.setType(ImageType.DESCRIPTION);
        Image old = imageRepository.findByDepartmentIdAndType(id, ImageType.DESCRIPTION);
        if (old != null) {
            imageRepository.delete(old);
        }
        imageRepository.save(image);
    }
}
