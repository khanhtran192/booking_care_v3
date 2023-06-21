package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DiagnoseRepository;
import com.mycompany.myapp.service.DiagnoseService;
import com.mycompany.myapp.service.dto.DiagnoseDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Diagnose}.
 */
@RestController
@RequestMapping("/api")
public class DiagnoseResource {

    private final Logger log = LoggerFactory.getLogger(DiagnoseResource.class);

    private static final String ENTITY_NAME = "diagnose";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DiagnoseService diagnoseService;

    private final DiagnoseRepository diagnoseRepository;

    public DiagnoseResource(DiagnoseService diagnoseService, DiagnoseRepository diagnoseRepository) {
        this.diagnoseService = diagnoseService;
        this.diagnoseRepository = diagnoseRepository;
    }

    /**
     * {@code POST  /diagnoses} : Create a new diagnose.
     *
     * @param diagnoseDTO the diagnoseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new diagnoseDTO, or with status {@code 400 (Bad Request)} if the diagnose has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/diagnoses")
    public ResponseEntity<DiagnoseDTO> createDiagnose(@RequestBody DiagnoseDTO diagnoseDTO) throws URISyntaxException {
        log.debug("REST request to save Diagnose : {}", diagnoseDTO);
        if (diagnoseDTO.getId() != null) {
            throw new BadRequestAlertException("A new diagnose cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DiagnoseDTO result = diagnoseService.save(diagnoseDTO);
        return ResponseEntity
            .created(new URI("/api/diagnoses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /diagnoses/:id} : Updates an existing diagnose.
     *
     * @param id the id of the diagnoseDTO to save.
     * @param diagnoseDTO the diagnoseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnoseDTO,
     * or with status {@code 400 (Bad Request)} if the diagnoseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the diagnoseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/diagnoses/{id}")
    public ResponseEntity<DiagnoseDTO> updateDiagnose(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DiagnoseDTO diagnoseDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Diagnose : {}, {}", id, diagnoseDTO);
        if (diagnoseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnoseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!diagnoseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DiagnoseDTO result = diagnoseService.update(diagnoseDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, diagnoseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /diagnoses/:id} : Partial updates given fields of an existing diagnose, field will ignore if it is null
     *
     * @param id the id of the diagnoseDTO to save.
     * @param diagnoseDTO the diagnoseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated diagnoseDTO,
     * or with status {@code 400 (Bad Request)} if the diagnoseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the diagnoseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the diagnoseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/diagnoses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DiagnoseDTO> partialUpdateDiagnose(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DiagnoseDTO diagnoseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Diagnose partially : {}, {}", id, diagnoseDTO);
        if (diagnoseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, diagnoseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!diagnoseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DiagnoseDTO> result = diagnoseService.partialUpdate(diagnoseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, diagnoseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /diagnoses} : get all the diagnoses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of diagnoses in body.
     */
    @GetMapping("/diagnoses")
    public ResponseEntity<List<DiagnoseDTO>> getAllDiagnoses(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Diagnoses");
        Page<DiagnoseDTO> page = diagnoseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /diagnoses/:id} : get the "id" diagnose.
     *
     * @param id the id of the diagnoseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the diagnoseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/diagnoses/{id}")
    public ResponseEntity<DiagnoseDTO> getDiagnose(@PathVariable Long id) {
        log.debug("REST request to get Diagnose : {}", id);
        Optional<DiagnoseDTO> diagnoseDTO = diagnoseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(diagnoseDTO);
    }

    /**
     * {@code DELETE  /diagnoses/:id} : delete the "id" diagnose.
     *
     * @param id the id of the diagnoseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/diagnoses/{id}")
    public ResponseEntity<Void> deleteDiagnose(@PathVariable Long id) {
        log.debug("REST request to delete Diagnose : {}", id);
        diagnoseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
