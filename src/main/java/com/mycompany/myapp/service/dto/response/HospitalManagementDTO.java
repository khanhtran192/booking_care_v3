package com.mycompany.myapp.service.dto.response;

import com.mycompany.myapp.domain.enumeration.FacilityType;
import javax.validation.constraints.NotNull;

public class HospitalManagementDTO {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String address;

    private String email;

    private String phoneNumber;

    private String description;

    private String workDay;

    private String workTime;

    private FacilityType type;

    private String procedure;
}
