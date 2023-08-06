package com.mycompany.myapp.service.dto.response;

import javax.validation.constraints.NotNull;

public class DepartmentResponseDTO {

    private Long id;

    @NotNull
    private String departmentName;

    private String description;
    private Boolean active;
    private HospitalInfoResponseDTO hospital;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public HospitalInfoResponseDTO getHospital() {
        return hospital;
    }

    public void setHospital(HospitalInfoResponseDTO hospital) {
        this.hospital = hospital;
    }
}
