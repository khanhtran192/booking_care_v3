package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.service.OrderService;
import com.mycompany.myapp.service.dto.OrderDTO;
import com.mycompany.myapp.service.dto.request.CreateOrderDTO;
import com.mycompany.myapp.service.dto.response.OrderResponseDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Order}.
 */
@RestController
@RequestMapping("/api")
public class OrderResource {

    private final Logger log = LoggerFactory.getLogger(OrderResource.class);

    private static final String ENTITY_NAME = "order";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderService orderService;

    private final OrderRepository orderRepository;

    public OrderResource(OrderService orderService, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @PutMapping("/order/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CreateOrderDTO orderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Order : {}, {}", id, orderDTO);
        OrderResponseDTO result = orderService.updateOrder(id, orderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/order/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable(value = "id", required = false) final Long id) throws URISyntaxException {
        log.debug("REST request to cancel Order : {}", id);
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code GET  /orders/:id} : get the "id" order.
     *
     * @param id the id of the orderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        Optional<OrderResponseDTO> orderDTO = orderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderDTO);
    }

    @GetMapping("/orders/personal")
    public ResponseEntity<List<OrderResponseDTO>> listOrderPersonal() {
        log.debug("REST request to get all Orders");
        List<OrderResponseDTO> orders = orderService.listOrderPersonal();
        return ResponseEntity.ok(orders);
    }
}
