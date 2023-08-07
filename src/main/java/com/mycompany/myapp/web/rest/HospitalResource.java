package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Pack;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.HospitalRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.*;
import com.mycompany.myapp.service.dto.AdminUserDTO;
import com.mycompany.myapp.service.dto.DepartmentDTO;
import com.mycompany.myapp.service.dto.DoctorDTO;
import com.mycompany.myapp.service.dto.HospitalDTO;
import com.mycompany.myapp.service.dto.request.CreateDoctorDTO;
import com.mycompany.myapp.service.dto.response.DepartmentResponseDTO;
import com.mycompany.myapp.service.dto.response.DoctorCreatedDTO;
import com.mycompany.myapp.service.dto.response.DoctorResponseDTO;
import com.mycompany.myapp.service.dto.response.PackResponseDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.EmailAlreadyUsedException;
import com.mycompany.myapp.web.rest.errors.LoginAlreadyUsedException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Hospital}.
 */
@RestController
@RequestMapping("/api")
public class HospitalResource {

    private final Logger log = LoggerFactory.getLogger(HospitalResource.class);

    private static final String ENTITY_NAME = "hospital";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HospitalService hospitalService;

    private final HospitalRepository hospitalRepository;
    private final DoctorService doctorService;

    private final PackService packService;

    private final DepartmentService departmentService;

    public HospitalResource(
        HospitalService hospitalService,
        HospitalRepository hospitalRepository,
        DoctorService doctorService,
        PackService packService,
        DepartmentService departmentService
    ) {
        this.hospitalService = hospitalService;
        this.hospitalRepository = hospitalRepository;
        this.doctorService = doctorService;
        this.packService = packService;
        this.departmentService = departmentService;
    }

    /**
     * {@code POST  /hospitals} : Create a new hospital.
     *
     * @param hospitalDTO the hospitalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hospitalDTO, or with status {@code 400 (Bad Request)} if the hospital has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    //    @PostMapping("/hospitals")
    //    public ResponseEntity<HospitalDTO> createHospital(@Valid @RequestBody HospitalDTO hospitalDTO) throws URISyntaxException {
    //        log.debug("REST request to save Hospital : {}", hospitalDTO);
    //        if (hospitalDTO.getId() != null) {
    //            throw new BadRequestAlertException("A new hospital cannot already have an ID", ENTITY_NAME, "idexists");
    //        }
    //        HospitalDTO result = hospitalService.save(hospitalDTO);
    //        return ResponseEntity
    //            .created(new URI("/api/hospitals/" + result.getId()))
    //            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
    //            .body(result);
    //    }

    /**
     * {@code PUT  /hospitals/:id} : Updates an existing hospital.
     *
     * @param id the id of the hospitalDTO to save.
     * @param hospitalDTO the hospitalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hospitalDTO,
     * or with status {@code 400 (Bad Request)} if the hospitalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hospitalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hospitals/{id}")
    public ResponseEntity<HospitalDTO> updateHospital(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HospitalDTO hospitalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Hospital : {}, {}", id, hospitalDTO);
        if (hospitalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hospitalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hospitalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HospitalDTO result = hospitalService.update(hospitalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hospitalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hospitals/:id} : Partial updates given fields of an existing hospital, field will ignore if it is null
     *
     * @param id the id of the hospitalDTO to save.
     * @param hospitalDTO the hospitalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hospitalDTO,
     * or with status {@code 400 (Bad Request)} if the hospitalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hospitalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hospitalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/hospitals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HospitalDTO> partialUpdateHospital(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HospitalDTO hospitalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Hospital partially : {}, {}", id, hospitalDTO);
        if (hospitalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hospitalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hospitalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HospitalDTO> result = hospitalService.partialUpdate(hospitalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hospitalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hospitals} : get all the hospitals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hospitals in body.
     */
    @GetMapping("/hospitals")
    public ResponseEntity<List<HospitalDTO>> getAllHospitalsForUser(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to get a page of Hospitals");
        Page<HospitalDTO> page = hospitalService.findAllForUser(pageable, keyword);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/hospitals/manage")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<HospitalDTO>> getAllHospitals(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to get a page of Hospitals");
        Page<HospitalDTO> page = hospitalService.findAll(pageable, keyword);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hospitals/:id} : get the "id" hospital.
     *
     * @param id the id of the hospitalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hospitalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hospitals/{id}")
    public ResponseEntity<HospitalDTO> getHospital(@PathVariable Long id) {
        log.debug("REST request to get Hospital : {}", id);
        Optional<HospitalDTO> hospitalDTO = hospitalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hospitalDTO);
    }

    /**
     * {@code DELETE  /hospitals/:id} : delete the "id" hospital.
     *
     * @param id the id of the hospitalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hospitals/manage/inactive/{id}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long id) {
        log.debug("REST request to delete Hospital : {}", id);
        hospitalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/hospitals/{id}/manage/doctors")
    public ResponseEntity<Page<DoctorResponseDTO>> getALlDoctorsByHospital(
        @PathVariable Integer id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to get all doctors in Hospital : {}", id);
        return ResponseEntity.ok().body(hospitalService.getAllDoctor(pageable, id, keyword));
    }

    @GetMapping("/hospitals/{id}/doctors")
    public ResponseEntity<Page<DoctorResponseDTO>> getALlDoctorsByHospitalForUser(
        @PathVariable Integer id,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to get all doctors in Hospital : {}", id);
        return ResponseEntity.ok().body(hospitalService.getAllDoctorForUser(pageable, id, keyword));
    }

    @GetMapping("/hospitals/{id}/manage/departments")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Page<DepartmentResponseDTO>> getAllDepartmentByHospital(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @PathVariable Long id,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to get all department in Hospital : {}", id);
        return ResponseEntity.ok().body(hospitalService.getAllDepartments(pageable, id, keyword));
    }

    @GetMapping("/hospitals/{id}/departments")
    public ResponseEntity<Page<DepartmentResponseDTO>> getAllDepartmentByHospitalForUser(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @PathVariable Long id,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to get all department in Hospital : {}", id);
        return ResponseEntity.ok().body(hospitalService.getAllDepartmentsForUser(pageable, id, keyword));
    }

    @PostMapping("/hospitals/manage/doctors/doctor")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<DoctorCreatedDTO>> createDoctor(@Valid @RequestBody List<CreateDoctorDTO> doctorDTO) {
        log.debug("REST request to save doctor : {}", doctorDTO);
        return ResponseEntity.ok().body(doctorService.createDoctor(doctorDTO));
    }

    @DeleteMapping("/hospitals/manage/doctors/{id}/inactive")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        log.debug("REST request to delete doctor : {}", id);
        doctorService.inactiveDoctorById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/hospitals/manage/hospital/{id}/active")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> activeHospital(@PathVariable Long id) {
        log.debug("REST request to active hospital : {}", id);
        hospitalService.activeHospital(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/hospitals/manage/doctors/{id}/active")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> activeDoctor(@PathVariable Long id) {
        log.debug("REST request to active doctor : {}", id);
        doctorService.acticeDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hospitals/{id}/packs")
    public ResponseEntity<Page<PackResponseDTO>> pagePackByHospitalForUser(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @PathVariable Long id,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to get all pack active in Hospital : {}", id);
        Page<PackResponseDTO> page = packService.findAll(pageable, true, id, keyword);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/hospitals/{id}/manage/packs")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Page<PackResponseDTO>> pagePackByHospital(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @PathVariable Long id,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to get all pack active in Hospital : {}", id);
        Page<PackResponseDTO> page = packService.findAll(pageable, null, id, keyword);
        return ResponseEntity.ok().body(page);
    }

    @DeleteMapping("/hospitals/manage/packs/{id}/inactive")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> inactivePack(@PathVariable Long id) {
        log.debug("REST request inactive pack : {}", id);
        packService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/hospitals/manage/packs/{id}/active")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> activePack(@PathVariable Long id) {
        log.debug("REST request active pack : {}", id);
        packService.activePack(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hospitals/manage/departments/{id}/inactive")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> inactiveDepartment(@PathVariable Long id) {
        log.debug("REST request inactive Department : {}", id);
        departmentService.inactiveDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/hospitals/manage/departments/{id}/active/")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> activeDepartment(@PathVariable Long id) {
        log.debug("REST request active Department : {}", id);
        departmentService.activeDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
