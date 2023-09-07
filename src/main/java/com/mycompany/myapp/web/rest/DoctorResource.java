package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.DoctorService;
import com.mycompany.myapp.service.OrderService;
import com.mycompany.myapp.service.TimeSlotService;
import com.mycompany.myapp.service.dto.FileDTO;
import com.mycompany.myapp.service.dto.request.CreateTimeSlotDTO;
import com.mycompany.myapp.service.dto.request.UpdateDoctorDTO;
import com.mycompany.myapp.service.dto.response.DoctorMostBookingDTO;
import com.mycompany.myapp.service.dto.response.DoctorResponseDTO;
import com.mycompany.myapp.service.dto.response.TimeSlotResponseDTO;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Doctor}.
 */
@RestController
@RequestMapping("/api")
public class DoctorResource {

    private final Logger log = LoggerFactory.getLogger(DoctorResource.class);

    private static final String ENTITY_NAME = "doctor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoctorService doctorService;

    private final DoctorRepository doctorRepository;
    private final TimeSlotService timeSlotService;
    private final OrderService orderService;

    public DoctorResource(
        DoctorService doctorService,
        DoctorRepository doctorRepository,
        TimeSlotService timeSlotService,
        OrderService orderService
    ) {
        this.doctorService = doctorService;
        this.doctorRepository = doctorRepository;
        this.timeSlotService = timeSlotService;
        this.orderService = orderService;
    }

    /**
     * {@code PUT  /doctors/:id} : Updates an existing doctor.
     *
     * @param id the id of the doctorDTO to save.
     * @param doctorDTO the doctorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doctorDTO,
     * or with status {@code 400 (Bad Request)} if the doctorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doctorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doctors/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UpdateDoctorDTO doctorDTO
    ) throws URISyntaxException {
        DoctorResponseDTO result = doctorService.update(doctorDTO, id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, String.valueOf(id)))
            .body(result);
    }

    /**
     * {@code GET  /doctors/:id} : get the "id" doctor.
     *
     * @param id the id of the doctorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doctorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctor(@PathVariable Long id) {
        log.debug("REST request to get Doctor : {}", id);
        Optional<DoctorResponseDTO> doctorDTO = doctorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorDTO);
    }

    @GetMapping("/doctors/most-rate")
    public ResponseEntity<List<DoctorResponseDTO>> listDoctorMostRate() {
        log.debug("REST request get list doctor most rate");
        return ResponseEntity.ok().body(doctorService.listDoctorMostStar());
    }

    @GetMapping("/doctors")
    public ResponseEntity<Page<DoctorResponseDTO>> getALlDoctorsByHospital(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to get all doctors");
        return ResponseEntity.ok().body(doctorService.pageAllDoctor(pageable, keyword));
    }

    @PutMapping("/doctors/time-slots/{id}")
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

    @PostMapping("/doctors/time-slots")
    public ResponseEntity<TimeSlotResponseDTO> createTimeSlot(@Valid @RequestBody CreateTimeSlotDTO timeSlotDTO) throws URISyntaxException {
        log.debug("REST request to save TimeSlot : {}", timeSlotDTO);
        TimeSlotResponseDTO result = timeSlotService.save(timeSlotDTO);
        return ResponseEntity
            .created(new URI("/api/time-slots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/doctors/{id}/manage/time-slots")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<TimeSlotResponseDTO>> allTimeSlotByDoctor(@PathVariable Long id) {
        log.debug("REST request to get all time slot active by doctor : {}", id);
        List<TimeSlotResponseDTO> list = timeSlotService.allTimeSlotByDoctor(id);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/doctors/{id}/time-slots")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<TimeSlotResponseDTO>> timeSlotActiveByDoctor(@PathVariable Long id) {
        log.debug("REST request to get all time slot active by doctor : {}", id);
        List<TimeSlotResponseDTO> list = timeSlotService.listTimeSlotByDoctor(id);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/doctors/doctors-most-stars")
    public ResponseEntity<List<DoctorResponseDTO>> listDoctorMostStar() {
        return ResponseEntity.ok().body(doctorService.listDoctorMostStar());
    }

    @GetMapping("/doctors/doctors-most-booking")
    public ResponseEntity<List<DoctorMostBookingDTO>> listDoctorMostBooking() {
        return ResponseEntity.ok().body(doctorService.listDoctorMostBooking());
    }

    @PutMapping("/doctors/{id}/rate")
    public ResponseEntity<Void> rateDoctor(@PathVariable Long id, @RequestParam Double rate) {
        doctorService.rateDoctor(id, rate);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/doctors/{id}/orders")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.DOCTOR + "\")")
    public ResponseEntity<Page> listOrdersByDoctor(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @PathVariable Long id
    ) {
        return ResponseEntity.ok().body(doctorService.findAllByDoctorAndStatus(id, pageable));
    }

    @GetMapping("/doctors/{id}/time-slot-free")
    public ResponseEntity<List<TimeSlotResponseDTO>> listTimeSlotFree(
        @PathVariable Long id,
        @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok().body(timeSlotService.listTimeSlotFreeOfDoctor(id, date));
    }

    @PutMapping("/doctors/orders/{id}/approved")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.DOCTOR + "\")")
    public ResponseEntity<Void> approveOrder(@PathVariable Long id) {
        orderService.approveOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/doctors/orders/{id}/rejected")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.DOCTOR + "\")")
    public ResponseEntity<Void> rejectedOrder(@PathVariable Long id) {
        orderService.rejectOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/doctors/{id}/manage/upload")
    //    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.ADMIN + "\" , \"" + AuthoritiesConstants.DOCTOR + "\")")
    public ResponseEntity<Void> uploadDoctorImage(@PathVariable Long id, @ModelAttribute FileDTO file) {
        doctorService.uploadAvatar(file, id);
        return ResponseEntity.noContent().build();
    }
}
