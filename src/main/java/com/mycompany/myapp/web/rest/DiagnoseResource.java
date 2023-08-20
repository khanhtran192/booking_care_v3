package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.DiagnoseRepository;
import com.mycompany.myapp.service.DiagnoseService;
import com.mycompany.myapp.service.dto.DiagnoseDTO;
import com.mycompany.myapp.service.dto.response.DiagnoseResponseDTO;
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
}
