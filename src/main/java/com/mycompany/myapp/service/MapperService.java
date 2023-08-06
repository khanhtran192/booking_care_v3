package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.domain.Hospital;
import com.mycompany.myapp.domain.Pack;
import com.mycompany.myapp.service.dto.response.DepartmentResponseDTO;
import com.mycompany.myapp.service.dto.response.DoctorResponseDTO;
import com.mycompany.myapp.service.dto.response.HospitalInfoResponseDTO;
import com.mycompany.myapp.service.dto.response.PackResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MapperService {

    private final Logger log = LoggerFactory.getLogger(MapperService.class);

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
}
