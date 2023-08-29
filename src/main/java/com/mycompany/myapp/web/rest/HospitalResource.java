package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.enumeration.FacilityType;
import com.mycompany.myapp.repository.DepartmentRepository;
import com.mycompany.myapp.repository.HospitalRepository;
import com.mycompany.myapp.repository.PackRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.*;
import com.mycompany.myapp.service.dto.*;
import com.mycompany.myapp.service.dto.request.*;
import com.mycompany.myapp.service.dto.response.*;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
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
    private final PackRepository packRepository;
    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;
    private final TimeSlotService timeSlotService;
    private final OrderService orderService;
    private final CustomerService customerService;

    public HospitalResource(
        HospitalService hospitalService,
        HospitalRepository hospitalRepository,
        DoctorService doctorService,
        PackService packService,
        DepartmentService departmentService,
        PackRepository packRepository,
        DepartmentRepository departmentRepository,
        TimeSlotService timeSlotService,
        OrderService orderService,
        CustomerService customerService
    ) {
        this.hospitalService = hospitalService;
        this.hospitalRepository = hospitalRepository;
        this.doctorService = doctorService;
        this.packService = packService;
        this.departmentService = departmentService;
        this.packRepository = packRepository;
        this.departmentRepository = departmentRepository;
        this.timeSlotService = timeSlotService;
        this.orderService = orderService;
        this.customerService = customerService;
    }

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
        //        if (hospitalDTO.getId() == null) {
        //            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        //        }
        //        if (!Objects.equals(id, hospitalDTO.getId())) {
        //            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        //        }
        //
        //        if (!hospitalRepository.existsById(id)) {
        //            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        //        }

        HospitalDTO result = hospitalService.update(hospitalDTO, id);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code GET  /hospitals} : get all the hospitals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hospitals in body.
     */
    @GetMapping("/hospitals")
    public ResponseEntity<Page<HospitalInfoResponseDTO>> getAllHospitalsForUser(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to get a page of Hospitals");
        Page<HospitalInfoResponseDTO> page = hospitalService.findAllForUser(pageable, keyword);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @GetMapping("/hospitals/manage")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Page<HospitalInfoResponseDTO>> getAllHospitals(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to get a page of Hospitals");
        Page<HospitalInfoResponseDTO> page = hospitalService.findAll(pageable, keyword);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    /**
     * {@code GET  /hospitals/:id} : get the "id" hospital.
     *
     * @param id the id of the hospitalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hospitalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hospitals/{id}")
    public ResponseEntity<HospitalInfoResponseDTO> getHospital(@PathVariable Long id) {
        log.debug("REST request to get Hospital : {}", id);
        HospitalInfoResponseDTO hospitalDTO = hospitalService.findOne(id);
        return ResponseEntity.ok().body(hospitalDTO);
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
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
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

    @PostMapping("/hospitals/manage/doctors")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<DoctorCreatedDTO> createDoctor(@Valid @RequestBody CreateDoctorDTO doctorDTO) {
        log.debug("REST request to save doctor : {}", doctorDTO);
        return ResponseEntity.ok().body(doctorService.createDoctorV2(doctorDTO));
    }

    @DeleteMapping("/hospitals/manage/doctors/{id}/inactive")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        log.debug("REST request to delete doctor : {}", id);
        doctorService.inactiveDoctorById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PutMapping("/hospitals/manage/hospital/{id}/active")
    //    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> activeHospital(@PathVariable Long id) {
        log.debug("REST request to active hospital : {}", id);
        hospitalService.activeHospital(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/hospitals/manage/doctors/{id}/active")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
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
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
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
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> inactivePack(@PathVariable Long id) {
        log.debug("REST request inactive pack : {}", id);
        packService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/hospitals/manage/packs/{id}/active")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> activePack(@PathVariable Long id) {
        log.debug("REST request active pack : {}", id);
        packService.activePack(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hospitals/manage/departments/{id}/inactive")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> inactiveDepartment(@PathVariable Long id) {
        log.debug("REST request inactive Department : {}", id);
        departmentService.inactiveDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/hospitals/manage/departments/{id}/active")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> activeDepartment(@PathVariable Long id) {
        log.debug("REST request active Department : {}", id);
        departmentService.activeDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/hospitals/manage/departments")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody CreateDepartmentDTO departmentDTO)
        throws URISyntaxException {
        log.debug("REST request to save Department : {}", departmentDTO);
        DepartmentResponseDTO result = departmentService.save(departmentDTO);
        return ResponseEntity
            .created(new URI("/api/departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/hospitals/manage/packs")
    public ResponseEntity<PackResponseDTO> createPack(@RequestBody CreatePackDTO packDTO) throws URISyntaxException {
        log.debug("REST request to save Pack : {}", packDTO);
        if (packDTO.getId() != null) {
            throw new BadRequestAlertException("A new pack cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PackResponseDTO result = packService.save(packDTO);
        return ResponseEntity
            .created(new URI("/api/packs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/hospitals/manage/packs/{id}")
    public ResponseEntity<PackResponseDTO> updatePack(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CreatePackDTO packDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pack : {}, {}", id, packDTO);
        //        if (packDTO.getId() == null) {
        //            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        //        }
        //        if (!Objects.equals(id, packDTO.getId())) {
        //            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        //        }
        //
        //        if (!packRepository.existsById(id)) {
        //            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        //        }

        PackResponseDTO result = packService.update(packDTO, id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/hospitals/manage/departments/{id}")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CreateDepartmentDTO departmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Department : {}, {}", id, departmentDTO);
        //        if (departmentDTO.getId() == null) {
        //            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        //        }
        //        if (!Objects.equals(id, departmentDTO.getId())) {
        //            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        //        }
        //
        //        if (!departmentRepository.existsById(id)) {
        //            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        //        }

        DepartmentResponseDTO result = departmentService.update(departmentDTO, id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(id)))
            .body(result);
    }

    @DeleteMapping("/hospitals/manage/packs/time-slots/{id}/inactive")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> inactiveTimeSlot(@PathVariable Long id) {
        log.debug("REST request inactive time slot : {}", id);
        timeSlotService.inactiveTimeSlot(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/hospitals/manage/packs/time-slots/{id}/active")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> activeTimeSlot(@PathVariable Long id) {
        log.debug("REST request inactive time slot : {}", id);
        timeSlotService.activeTimeSlot(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hospitals/manage/packs/{id}/time-slots")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<TimeSlotResponseDTO>> allTimeSlotByPack(@PathVariable Long id) {
        log.debug("REST request to get all time slot in Pack : {}", id);
        List<TimeSlotResponseDTO> list = timeSlotService.allTimeSlotByPack(id);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/hospitals/packs/{id}/time-slots")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<TimeSlotResponseDTO>> timeSlotActiveByPack(@PathVariable Long id) {
        log.debug("REST request to get all time slot active in Pack : {}", id);
        List<TimeSlotResponseDTO> list = timeSlotService.listTimeSlotByPack(id);
        return ResponseEntity.ok().body(list);
    }

    @PutMapping("/hospitals/manage/packs/time-slots/{id}")
    public ResponseEntity<TimeSlotResponseDTO> updateTimeSlot(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CreateTimeSlotDTO timeSlotDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TimeSlot : {}, {}", id, timeSlotDTO);
        TimeSlotResponseDTO result = timeSlotService.update(timeSlotDTO, id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(id)))
            .body(result);
    }

    @PostMapping("/hospitals/manage/packs/time-slots")
    public ResponseEntity<TimeSlotResponseDTO> createTimeSlot(@Valid @RequestBody CreateTimeSlotDTO timeSlotDTO) throws URISyntaxException {
        log.debug("REST request to save TimeSlot : {}", timeSlotDTO);
        TimeSlotResponseDTO result = timeSlotService.save(timeSlotDTO);
        return ResponseEntity
            .created(new URI("/api/time-slots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/hospitals/facility-type")
    public ResponseEntity<List<FacilityType>> listFacilities() {
        return ResponseEntity.ok(hospitalService.listFacilities());
    }

    @PostMapping("/hospitals/{id}/manage/upload/logo")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\" , \"" + AuthoritiesConstants.DOCTOR + "\")")
    public ResponseEntity<Void> uploadHospitalLogo(@PathVariable Long id, @ModelAttribute FileDTO file) {
        hospitalService.uploadLogo(id, file);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/hospitals/{id}/manage/upload/background")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\" , \"" + AuthoritiesConstants.DOCTOR + "\")")
    public ResponseEntity<Void> uploadHospitalBackground(@PathVariable Long id, @ModelAttribute FileDTO file) {
        hospitalService.uploadBackground(id, file);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/hospitals/manage/departments/{id}/upload/logo")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\" , \"" + AuthoritiesConstants.HOSPITAL + "\")")
    public ResponseEntity<Void> uploadDepartmentLogo(@PathVariable Long id, @ModelAttribute FileDTO file) {
        departmentService.uploadLogo(id, file);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/hospitals/manage/departments/{id}/upload/background")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\" , \"" + AuthoritiesConstants.HOSPITAL + "\")")
    public ResponseEntity<Void> uploadDepartmentBackground(@PathVariable Long id, @ModelAttribute FileDTO file) {
        departmentService.uploadBackground(id, file);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/hospitals/manage/packs/{id}/upload/logo")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\" , \"" + AuthoritiesConstants.HOSPITAL + "\")")
    public ResponseEntity<Void> uploadPackLogo(@PathVariable Long id, @ModelAttribute FileDTO file) {
        packService.uploadLogo(id, file);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hospitals/{id}/manage/customer")
    public ResponseEntity<Page<CustomerResponseDTO>> getCustomerByHospital(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @PathVariable Long id
    ) {
        Page<CustomerResponseDTO> page = customerService.listCustomerByHospial(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @GetMapping("/hospitals/packs/{id}/time-slot-free")
    public ResponseEntity<List<TimeSlotResponseDTO>> listTimeSlotFree(
        @PathVariable Long id,
        @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok().body(timeSlotService.listTimeSlotFreeOfPack(id, date));
    }

    @PostMapping("/hospitals/doctors/{id}/booking")
    public ResponseEntity<OrderResponseDTO> createOrderDoctor(@PathVariable Long id, @RequestBody CreateOrderDTO request) {
        return ResponseEntity.ok().body(orderService.createOrderDoctor(id, request));
    }

    @PostMapping("/hospitals/packs/{id}/booking")
    public ResponseEntity<OrderResponseDTO> createOrderPack(@PathVariable Long id, @RequestBody CreateOrderDTO request) {
        return ResponseEntity.ok().body(orderService.createOrderPack(id, request));
    }

    @GetMapping("/hospitals/{id}/manage/order")
    public ResponseEntity<Page<OrderResponseDTO>> getOrderByHospital(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @PathVariable Long id
    ) {
        Page<OrderResponseDTO> page = hospitalService.orderByHospital(id, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }
}
