package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DoctorRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.DoctorService;
import com.mycompany.myapp.service.TimeSlotService;
import com.mycompany.myapp.service.dto.DoctorDTO;
import com.mycompany.myapp.service.dto.request.CreateTimeSlotDTO;
import com.mycompany.myapp.service.dto.response.DoctorResponseDTO;
import com.mycompany.myapp.service.dto.response.TimeSlotResponseDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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

    public DoctorResource(DoctorService doctorService, DoctorRepository doctorRepository, TimeSlotService timeSlotService) {
        this.doctorService = doctorService;
        this.doctorRepository = doctorRepository;
        this.timeSlotService = timeSlotService;
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
    public ResponseEntity<DoctorDTO> updateDoctor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DoctorDTO doctorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Doctor : {}, {}", id, doctorDTO);
        if (doctorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doctorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doctorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DoctorDTO result = doctorService.update(doctorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doctorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /doctors/:id} : get the "id" doctor.
     *
     * @param id the id of the doctorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doctorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorDTO> getDoctor(@PathVariable Long id) {
        log.debug("REST request to get Doctor : {}", id);
        Optional<DoctorDTO> doctorDTO = doctorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doctorDTO);
    }

    @GetMapping("/doctors/most-rate")
    public ResponseEntity<List<DoctorResponseDTO>> listDoctorMostRate() {
        log.debug("REST request get list doctor most rate");
        return ResponseEntity.ok().body(doctorService.listDoctorMostRate());
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
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<TimeSlotResponseDTO>> allTimeSlotByDoctor(@PathVariable Long id) {
        log.debug("REST request to get all time slot active by doctor : {}", id);
        List<TimeSlotResponseDTO> list = timeSlotService.allTimeSlotByDoctor(id);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/doctors/{id}/time-slots")
    @PreAuthorize("hasAnyAuthority(\"" + AuthoritiesConstants.HOSPITAL + "\" , \"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<TimeSlotResponseDTO>> timeSlotActiveByDoctor(@PathVariable Long id) {
        log.debug("REST request to get all time slot active by doctor : {}", id);
        List<TimeSlotResponseDTO> list = timeSlotService.listTimeSlotByDoctor(id);
        return ResponseEntity.ok().body(list);
    }
}
