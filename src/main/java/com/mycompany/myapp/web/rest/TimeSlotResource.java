package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.enumeration.TimeSlotValue;
import com.mycompany.myapp.repository.TimeSlotRepository;
import com.mycompany.myapp.service.TimeSlotService;
import com.mycompany.myapp.service.dto.TimeSlotDTO;
import com.mycompany.myapp.service.dto.request.CreateTimeSlotDTO;
import com.mycompany.myapp.service.dto.response.TimeSlotResponseDTO;
import com.mycompany.myapp.service.dto.response.TimeSlotValueResponseDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.TimeSlot}.
 */
@RestController
@RequestMapping("/api")
public class TimeSlotResource {

    private final Logger log = LoggerFactory.getLogger(TimeSlotResource.class);

    private final TimeSlotService timeSlotService;

    public TimeSlotResource(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    /**
     * {@code GET  /time-slots/:id} : get the "id" timeSlot.
     *
     * @param id the id of the timeSlotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeSlotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/time-slots/{id}")
    public ResponseEntity<TimeSlotResponseDTO> getTimeSlot(@PathVariable Long id) {
        log.debug("REST request to get TimeSlot : {}", id);
        Optional<TimeSlotResponseDTO> timeSlotDTO = timeSlotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeSlotDTO);
    }

    @GetMapping("/time-slots/time-slot-values")
    public ResponseEntity<List<TimeSlotValueResponseDTO>> getTimeSlot() {
        return ResponseEntity.ok().body(timeSlotService.timeSlotValues());
    }
}
