package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.PackRepository;
import com.mycompany.myapp.service.PackService;
import com.mycompany.myapp.service.dto.response.PackResponseDTO;
import java.util.List;
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
}
