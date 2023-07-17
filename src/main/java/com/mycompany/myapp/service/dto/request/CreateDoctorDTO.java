package com.mycompany.myapp.service.dto.request;

import com.mycompany.myapp.service.dto.DepartmentDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDoctorDTO {

    private String login;
    private String firstName;
    private String lastName;
    private String name;
    private String email;
    private boolean activated = false;
    private DepartmentDTO department;
    private Integer hospitalId;
}
