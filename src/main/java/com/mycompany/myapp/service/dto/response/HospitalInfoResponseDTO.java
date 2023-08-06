package com.mycompany.myapp.service.dto.response;

import com.mycompany.myapp.domain.enumeration.FacilityType;
import javax.validation.constraints.NotNull;

public class HospitalInfoResponseDTO {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String address;

    private String email;

    private String phoneNumber;

    private FacilityType type;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public FacilityType getType() {
        return type;
    }

    public void setType(FacilityType type) {
        this.type = type;
    }
}
