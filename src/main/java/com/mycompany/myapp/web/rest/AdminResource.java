package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.DataService;
import com.mycompany.myapp.service.HospitalService;
import com.mycompany.myapp.service.MailService;
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

    private final HospitalService hospitalService;

    private final MailService mailService;

    private final UserService userService;
    private final DataService dataService;

    public AdminResource(HospitalService hospitalService, UserService userService, MailService mailService, DataService dataService) {
        this.hospitalService = hospitalService;
        this.userService = userService;
        this.mailService = mailService;
        this.dataService = dataService;
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

    @GetMapping("/admin/add-hospital")
    public ResponseEntity<Void> addHospital() {
        //                dataService.createAccountHospital();
        //                dataService.createPack();
        //                dataService.createDepartment();
        //        dataService.createAccountDoctor();
        //        dataService.createDocTor();
        //        dataService.createAccountDoctor();
        //        dataService.createDataTimeSlot();
        dataService.createDianose();
        return ResponseEntity.noContent().build();
    }
}
