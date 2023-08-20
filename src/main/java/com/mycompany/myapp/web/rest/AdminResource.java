package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.service.dto.request.CreateHospitalDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/admin")
public class AdminResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    public AdminResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/hospitals")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> createHospital(@RequestBody CreateHospitalDTO createHospitalDTO) {
        log.debug("REST request to create hospital: {}", createHospitalDTO.getName());
        userService.createHospital(createHospitalDTO);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", createHospitalDTO.getName()))
            .build();
    }
}
