package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.domain.Image;
import com.mycompany.myapp.domain.enumeration.ImageType;
import com.mycompany.myapp.domain.enumeration.OrderStatus;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.*;
import com.mycompany.myapp.service.dto.FileDTO;
import com.mycompany.myapp.service.dto.request.CreateDoctorDTO;
import com.mycompany.myapp.service.dto.request.UpdateDoctorDTO;
import com.mycompany.myapp.service.dto.response.DoctorCreatedDTO;
import com.mycompany.myapp.service.dto.response.DoctorMostBookingDTO;
import com.mycompany.myapp.service.dto.response.DoctorResponseDTO;
import com.mycompany.myapp.service.dto.response.OrderResponseDTO;
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

    private final DepartmentRepository departmentRepository;

    private final MapperService mapperService;

    private final UserService userService;

    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;
    private final DepartmentMapper departmentMapper;
    private final OrderRepository orderRepository;
    private final MinioService minioService;
    private final ImageRepository imageRepository;

    public DoctorService(
        DoctorRepository doctorRepository,
        DoctorMapper doctorMapper,
        DepartmentRepository departmentRepository,
        UserService userService,
        HospitalRepository hospitalRepository,
        HospitalMapper hospitalMapper,
        DepartmentMapper departmentMapper,
        MapperService mapperService,
        OrderRepository orderRepository,
        MinioService minioService,
        ImageRepository imageRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.departmentRepository = departmentRepository;
        this.userService = userService;
        this.hospitalRepository = hospitalRepository;
        this.hospitalMapper = hospitalMapper;
        this.departmentMapper = departmentMapper;
        this.mapperService = mapperService;
        this.orderRepository = orderRepository;
        this.minioService = minioService;
        this.imageRepository = imageRepository;
    }

    /**
     * Update a doctor.
     *
     * @param doctorDTO the entity to save.
     * @return the persisted entity.
     */
    public DoctorResponseDTO update(UpdateDoctorDTO doctorDTO, Long id) {
        log.debug("Request to update Doctor : {}", doctorDTO);
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor not found"));
        Department department = departmentRepository
            .findById(doctorDTO.getDepartmentId())
            .orElseThrow(() -> new NotFoundException("Department not found"));
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setName(doctorDTO.getName());
        doctor.setPhoneNumber(doctorDTO.getPhoneNumber());
        doctor.setDegree(doctorDTO.getDegree());
        doctor.setSpecialize(doctorDTO.getSpecialize());
        doctor.setDateOfBirth(doctorDTO.getDateOfBirth());
        doctor.setDepartment(department);
        doctor = doctorRepository.save(doctor);
        return mapperService.mapToDto(doctor);
    }

    /**
     * Get one doctor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DoctorResponseDTO> findOne(Long id) {
        log.debug("Request to get Doctor : {}", id);
        return doctorRepository.findById(id).map(mapperService::mapToDto);
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

    public Page<DoctorResponseDTO> findAllByHospitalId(Pageable pageable, Integer hospitalId, String keyword) {
        return doctorRepository.pageDoctorByHospital(pageable, hospitalId, keyword).map(mapperService::mapToDto);
    }

    public Page<DoctorResponseDTO> findAllByHospitalIdForUser(Pageable pageable, Integer hospitalId, String keyword) {
        return doctorRepository.pageDoctorByHospitalForUser(pageable, hospitalId, keyword).map(mapperService::mapToDto);
    }

    public Page<DoctorResponseDTO> findAllByDepartment(Pageable pageable, Department department) {
        return doctorRepository.findAllByDepartment(pageable, department).map(mapperService::mapToDto);
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
        userService.activeDoctor(doctor.getId());
    }

    public List<DoctorResponseDTO> listDoctorMostStar() {
        List<Doctor> doctors = doctorRepository
            .findAll()
            .stream()
            .filter(Doctor::getActive)
            .sorted((Comparator.comparing(Doctor::getStar)).reversed())
            .limit(5)
            .collect(Collectors.toList());
        return doctors.stream().map(mapperService::mapToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponseDTO> pageAllDoctor(Pageable pageable, String keyword) {
        return doctorRepository.pageDoctorForUser(pageable, keyword).map(mapperService::mapToDto);
    }

    public void rateDoctor(Long id, Double rate) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor: " + id + " not found"));
        Double newRate = doctor.getRate() + 1;
        Double newStar = ((doctor.getStar() * doctor.getRate()) + rate) / newRate;
        doctor.setRate(newRate);
        doctor.setStar(newStar);
        doctorRepository.save(doctor);
    }

    public List<DoctorMostBookingDTO> listDoctorMostBooking() {
        List<Doctor> doctors = doctorRepository.findAllByActiveIsTrue();
        return doctors
            .stream()
            .map(doctor -> {
                DoctorMostBookingDTO p = new DoctorMostBookingDTO();
                p.setDoctor(mapperService.mapToDto(doctor));
                p.setQuantity(
                    orderRepository
                        .orderByDoctor(
                            doctor,
                            Arrays.asList(OrderStatus.COMPLETE, OrderStatus.PENDING, OrderStatus.APPROVED, OrderStatus.REJECTED)
                        )
                        .size()
                );
                return p;
            })
            .sorted((Comparator.comparing(DoctorMostBookingDTO::getQuantity)).reversed())
            .collect(Collectors.toList());
    }

    public Page<OrderResponseDTO> findAllByDoctorAndStatus(Long id, List<String> status, Pageable pageable) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor not found"));
        try {
            List<OrderStatus> orderStatuses = status.stream().map(OrderStatus::valueOf).collect(Collectors.toList());
            return orderRepository.findAllbyDoctorAndStatus(doctor, orderStatuses, pageable).map(mapperService::mapToDto);
        } catch (Exception e) {
            log.error("get all order by doctor {} error: {}", doctor.getName(), e.getMessage());
            return null;
        }
    }

    public void uploadAvatar(FileDTO file, Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor not found"));
        String name = minioService.uploadFile(file);
        String path = minioService.getObject(name);
        Image image = new Image();
        image.setName(name);
        image.setType(ImageType.AVATAR);
        image.setPath(path);
        image.setDoctorId(doctor.getId());
        Image old = imageRepository.findByDoctorIdAndType(id, ImageType.AVATAR);
        if (old != null) {
            imageRepository.delete(old);
        }
        imageRepository.save(image);
    }
}
