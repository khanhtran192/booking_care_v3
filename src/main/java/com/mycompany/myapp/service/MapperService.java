package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.enumeration.TimeSlotValue;
import com.mycompany.myapp.exception.BadRequestException;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.repository.PackRepository;
import com.mycompany.myapp.service.dto.request.CreateTimeSlotDTO;
import com.mycompany.myapp.service.dto.response.*;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MapperService {

    private final Logger log = LoggerFactory.getLogger(MapperService.class);
    private final PackRepository packRepository;

    private final DoctorRepository doctorRepository;

    public MapperService(PackRepository packRepository, DoctorRepository doctorRepository) {
        this.packRepository = packRepository;
        this.doctorRepository = doctorRepository;
    }

    public HospitalInfoResponseDTO mapToDto(Hospital hospital) {
        try {
            if (hospital == null) {
                return null;
            }
            HospitalInfoResponseDTO hospitalInfoResponseDTO = new HospitalInfoResponseDTO();
            hospitalInfoResponseDTO.setId(hospital.getId());
            hospitalInfoResponseDTO.setName(hospital.getName());
            hospitalInfoResponseDTO.setAddress(hospital.getAddress());
            hospitalInfoResponseDTO.setEmail(hospital.getEmail());
            hospitalInfoResponseDTO.setPhoneNumber(hospital.getPhoneNumber());
            return hospitalInfoResponseDTO;
        } catch (Exception e) {
            log.error("map to hospital dto info error: {}", e.getMessage());
            return null;
        }
    }

    public DepartmentResponseDTO mapToDto(Department department) {
        try {
            if (department == null) {
                return null;
            }
            DepartmentResponseDTO departmentResponseDTO = new DepartmentResponseDTO();
            departmentResponseDTO.setDepartmentName(department.getDepartmentName());
            departmentResponseDTO.setDescription(department.getDescription());
            departmentResponseDTO.setId(department.getId());
            departmentResponseDTO.setHospital(mapToDto(department.getHospital()));
            departmentResponseDTO.setActive(department.getActive());
            return departmentResponseDTO;
        } catch (Exception e) {
            log.error("map to department dto error: {}", e.getMessage());
            return null;
        }
    }

    public DoctorResponseDTO mapToDto(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        DoctorResponseDTO doctorResponseDTO = new DoctorResponseDTO();
        doctorResponseDTO.setId(doctor.getId());
        doctorResponseDTO.setName(doctor.getName());
        doctorResponseDTO.setEmail(doctor.getEmail());
        doctorResponseDTO.setDateOfBirth(doctor.getDateOfBirth());
        doctorResponseDTO.setPhoneNumber(doctor.getPhoneNumber());
        doctorResponseDTO.setDegree(doctor.getDegree());
        doctorResponseDTO.setSpecialize(doctor.getSpecialize());
        doctorResponseDTO.setDepartment(mapToDto(doctor.getDepartment()));
        return doctorResponseDTO;
    }

    public PackResponseDTO mapToDto(Pack pack) {
        log.debug("map {} to dto", pack);
        if (pack == null) {
            return null;
        }
        PackResponseDTO dto = new PackResponseDTO();
        dto.setId(pack.getId());
        dto.setName(pack.getName());
        dto.setDescription(pack.getDescription());
        dto.setPrice(pack.getPrice());
        dto.setHospital(mapToDto(pack.getHospital()));
        return dto;
    }

    public TimeSlot mapToEntity(CreateTimeSlotDTO createTimeSlotDTO) {
        TimeSlot timeSlot = new TimeSlot();
        if (createTimeSlotDTO.getPackId() == null && createTimeSlotDTO.getDoctorId() == null) {
            throw new NotFoundException("Pack and doctor can not be null");
        } else if (createTimeSlotDTO.getPackId() != null && createTimeSlotDTO.getDoctorId() != null) {
            throw new BadRequestException("can not has both pack and doctor");
        } else if (
            createTimeSlotDTO.getStartTime() == null && createTimeSlotDTO.getEndTime() == null && createTimeSlotDTO.getTime() == null
        ) {
            throw new BadRequestException("time cannot be null");
        }
        if (createTimeSlotDTO.getStartTime() != null && createTimeSlotDTO.getEndTime() != null && createTimeSlotDTO.getTime() == null) {
            if (
                TimeSlotValue.valueOf(createTimeSlotDTO.getStartTime()).getNumber() >=
                TimeSlotValue.valueOf(createTimeSlotDTO.getEndTime()).getNumber()
            ) {
                throw new BadRequestException("Start time cannot equal or after end time");
            }
            timeSlot.setStartTime(TimeSlotValue.valueOf(createTimeSlotDTO.getStartTime()));
            timeSlot.setEndTime(TimeSlotValue.valueOf(createTimeSlotDTO.getEndTime()));
            timeSlot.setTime(
                TimeSlotValue.valueOf(createTimeSlotDTO.getStartTime()).getValue() +
                " : " +
                TimeSlotValue.valueOf(createTimeSlotDTO.getEndTime()).getValue()
            );
        } else if (
            (createTimeSlotDTO.getStartTime() != null || createTimeSlotDTO.getEndTime() != null) && createTimeSlotDTO.getTime() != null
        ) {
            throw new BadRequestException("Invalid time slot");
        } else if (createTimeSlotDTO.getTime() != null) {
            if (Arrays.asList("Cả ngày", "Buổi sáng", "Buổi chiều").contains(createTimeSlotDTO.getTime())) {
                throw new BadRequestException("If start and end time be null, time only can be Cả ngày, Buổi sáng or Buổi chiều");
            }
            timeSlot.setTime(TimeSlotValue.valueOf(createTimeSlotDTO.getTime()).getValue());
            timeSlot.setStartTime(null);
            timeSlot.setEndTime(null);
        }
        timeSlot.setPrice(createTimeSlotDTO.getPrice());
        timeSlot.setDescription(createTimeSlotDTO.getDescription());
        timeSlot.setStatus(createTimeSlotDTO.getStatus());
        if (createTimeSlotDTO.getPackId() != null && createTimeSlotDTO.getPackId() != 0) {
            timeSlot.setPack(
                packRepository
                    .findById(createTimeSlotDTO.getPackId())
                    .orElseThrow(() -> new NotFoundException("Pack: " + createTimeSlotDTO.getPackId() + "not found"))
            );
        } else if (createTimeSlotDTO.getDoctorId() != null && createTimeSlotDTO.getDoctorId() != 0) {
            timeSlot.setDoctor(
                doctorRepository
                    .findById(createTimeSlotDTO.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Doctor: " + createTimeSlotDTO.getDoctorId() + "not found"))
            );
        }
        return timeSlot;
    }

    public TimeSlot mapToEntity(CreateTimeSlotDTO createTimeSlotDTO, TimeSlot timeSlot) {
        if (createTimeSlotDTO.getPackId() == null && createTimeSlotDTO.getDoctorId() == null) {
            throw new NotFoundException("Pack and doctor can not be null");
        } else if (createTimeSlotDTO.getPackId() != null && createTimeSlotDTO.getDoctorId() != null) {
            throw new BadRequestException("can not has both pack and doctor");
        } else if (
            createTimeSlotDTO.getStartTime() == null && createTimeSlotDTO.getEndTime() == null && createTimeSlotDTO.getTime() == null
        ) {
            throw new BadRequestException("time cannot be null");
        }
        if (createTimeSlotDTO.getStartTime() != null && createTimeSlotDTO.getEndTime() != null && createTimeSlotDTO.getTime() == null) {
            if (
                TimeSlotValue.valueOf(createTimeSlotDTO.getStartTime()).getNumber() >=
                TimeSlotValue.valueOf(createTimeSlotDTO.getEndTime()).getNumber()
            ) {
                throw new BadRequestException("Start time cannot equal or after end time");
            }
            timeSlot.setStartTime(TimeSlotValue.valueOf(createTimeSlotDTO.getStartTime()));
            timeSlot.setEndTime(TimeSlotValue.valueOf(createTimeSlotDTO.getEndTime()));
            timeSlot.setTime(
                TimeSlotValue.valueOf(createTimeSlotDTO.getStartTime()).getValue() +
                " : " +
                TimeSlotValue.valueOf(createTimeSlotDTO.getEndTime()).getValue()
            );
        } else if (
            (createTimeSlotDTO.getStartTime() != null || createTimeSlotDTO.getEndTime() != null) && createTimeSlotDTO.getTime() != null
        ) {
            throw new BadRequestException("Invalid time slot");
        } else if (createTimeSlotDTO.getTime() != null) {
            if (Arrays.asList("Cả ngày", "Buổi sáng", "Buổi chiều").contains(createTimeSlotDTO.getTime())) {
                throw new BadRequestException("If start and end time be null, time only can be Cả ngày, Buổi sáng or Buổi chiều");
            }
            timeSlot.setTime(TimeSlotValue.valueOf(createTimeSlotDTO.getTime()).getValue());
            timeSlot.setStartTime(null);
            timeSlot.setEndTime(null);
        }
        timeSlot.setPrice(createTimeSlotDTO.getPrice());
        timeSlot.setDescription(createTimeSlotDTO.getDescription());
        timeSlot.setStatus(createTimeSlotDTO.getStatus());
        if (createTimeSlotDTO.getPackId() != null && createTimeSlotDTO.getPackId() != 0) {
            timeSlot.setPack(
                packRepository
                    .findById(createTimeSlotDTO.getPackId())
                    .orElseThrow(() -> new NotFoundException("Pack: " + createTimeSlotDTO.getPackId() + "not found"))
            );
        } else if (createTimeSlotDTO.getDoctorId() != null && createTimeSlotDTO.getDoctorId() != 0) {
            timeSlot.setDoctor(
                doctorRepository
                    .findById(createTimeSlotDTO.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Doctor: " + createTimeSlotDTO.getDoctorId() + "not found"))
            );
        }
        return timeSlot;
    }

    public TimeSlotResponseDTO mapToDto(TimeSlot timeSlot) {
        try {
            TimeSlotResponseDTO timeSlotResponseDTO = new TimeSlotResponseDTO();
            timeSlotResponseDTO.setId(timeSlot.getId());
            timeSlotResponseDTO.setDescription(timeSlot.getDescription());
            timeSlotResponseDTO.setStatus(timeSlot.getStatus());
            timeSlotResponseDTO.setPrice(timeSlot.getPrice());
            timeSlotResponseDTO.setPack(mapToDto(timeSlot.getPack()));
            timeSlotResponseDTO.setDoctor(mapToDto(timeSlot.getDoctor()));
            if (timeSlot.getStartTime() != null) {
                timeSlotResponseDTO.setStartTime(mapToDto(timeSlot.getStartTime()));
            } else if (timeSlot.getEndTime() != null) {
                timeSlotResponseDTO.setEndTime(mapToDto(timeSlot.getEndTime()));
            }
            return timeSlotResponseDTO;
        } catch (Exception e) {
            log.error("map time slot to dto failed: {}", e.getMessage());
            return null;
        }
    }

    public TimeSlotValueResponseDTO mapToDto(TimeSlotValue timeSlotValue) {
        TimeSlotValueResponseDTO timeSlotValueResponseDTO = new TimeSlotValueResponseDTO();
        timeSlotValueResponseDTO.setTimeSlot(timeSlotValue.name());
        timeSlotValueResponseDTO.setValue(timeSlotValue.getValue());
        timeSlotValueResponseDTO.setNumber(timeSlotValue.getNumber());
        return timeSlotValueResponseDTO;
    }
}
