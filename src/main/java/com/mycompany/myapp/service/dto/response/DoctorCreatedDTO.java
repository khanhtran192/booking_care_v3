package com.mycompany.myapp.service.dto.response;

import com.mycompany.myapp.service.dto.DepartmentDTO;
import com.mycompany.myapp.service.dto.HospitalDTO;

public class DoctorCreatedDTO {

    private String name;
    private String email;
    private DepartmentDTO department;
    private HospitalDTO hospital;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public HospitalDTO getHospital() {
        return hospital;
    }

    public void setHospital(HospitalDTO hospital) {
        this.hospital = hospital;
    }
}
