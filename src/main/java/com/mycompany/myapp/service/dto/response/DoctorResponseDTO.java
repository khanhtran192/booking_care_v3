package com.mycompany.myapp.service.dto.response;

import com.mycompany.myapp.service.dto.DepartmentDTO;
import com.mycompany.myapp.service.dto.HospitalDTO;
import java.time.Instant;

public class DoctorResponseDTO {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private Instant dateOfBirth;

    private String degree;

    private Double rate;

    private String specialize;

    private DepartmentResponseDTO department;

    //    private HospitalDTO hospital;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    //    public HospitalDTO getHospital() {
    //        return hospital;
    //    }
    //
    //    public void setHospital(HospitalDTO hospital) {
    //        this.hospital = hospital;
    //    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getSpecialize() {
        return specialize;
    }

    public void setSpecialize(String specialize) {
        this.specialize = specialize;
    }

    public DepartmentResponseDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentResponseDTO department) {
        this.department = department;
    }
}
