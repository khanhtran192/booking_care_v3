package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.PackRepository;
import com.mycompany.myapp.service.PackService;
import com.mycompany.myapp.service.dto.PackDTO;
import com.mycompany.myapp.service.dto.request.CreatePackDTO;
import com.mycompany.myapp.service.dto.response.PackResponseDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Pack}.
 */
@RestController
@RequestMapping("/api")
public class PackResource {

    private final Logger log = LoggerFactory.getLogger(PackResource.class);

    private static final String ENTITY_NAME = "pack";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackService packService;

    private final PackRepository packRepository;

    public PackResource(PackService packService, PackRepository packRepository) {
        this.packService = packService;
        this.packRepository = packRepository;
    }

    /**
     * {@code POST  /packs} : Create a new pack.
     *
     * @param packDTO the packDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packDTO, or with status {@code 400 (Bad Request)} if the pack has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    /**
     * {@code PUT  /packs/:id} : Updates an existing pack.
     *
     * @param id the id of the packDTO to save.
     * @param packDTO the packDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packDTO,
     * or with status {@code 400 (Bad Request)} if the packDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/packs/{id}")
    public ResponseEntity<PackResponseDTO> updatePack(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CreatePackDTO packDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pack : {}, {}", id, packDTO);
        if (packDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PackResponseDTO result = packService.update(packDTO, id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /packs/:id} : Partial updates given fields of an existing pack, field will ignore if it is null
     *
     * @param id the id of the packDTO to save.
     * @param packDTO the packDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packDTO,
     * or with status {@code 400 (Bad Request)} if the packDTO is not valid,
     * or with status {@code 404 (Not Found)} if the packDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the packDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/packs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PackDTO> partialUpdatePack(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PackDTO packDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pack partially : {}, {}", id, packDTO);
        if (packDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PackDTO> result = packService.partialUpdate(packDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /packs} : get all the packs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packs in body.
     */
    @GetMapping("/packs")
    public ResponseEntity<List<PackResponseDTO>> getAllPacksForUser(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "keyword", required = false) String keyword
    ) {
        log.debug("REST request to get a page of Packs");
        Page<PackResponseDTO> page = packService.findAll(pageable, true, null, keyword);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /packs/:id} : get the "id" pack.
     *
     * @param id the id of the packDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/packs/{id}")
    public ResponseEntity<PackResponseDTO> getPack(@PathVariable Long id) {
        log.debug("REST request to get Pack : {}", id);
        Optional<PackResponseDTO> packDTO = packService.findOne(id);
        return ResponseUtil.wrapOrNotFound(packDTO);
    }
    /**
     * {@code DELETE  /packs/:id} : delete the "id" pack.
     *
     * @param id the id of the packDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    //    @DeleteMapping("/packs/{id}")
    //    public ResponseEntity<Void> deletePack(@PathVariable Long id) {
    //        log.debug("REST request to delete Pack : {}", id);
    //        packService.delete(id);
    //        return ResponseEntity
    //            .noContent()
    //            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
    //            .build();
    //    }
}
