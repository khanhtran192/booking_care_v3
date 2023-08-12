package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.enumeration.OrderStatus;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.service.dto.OrderDTO;
import com.mycompany.myapp.service.dto.request.CreateOrderDTO;
import com.mycompany.myapp.service.mapper.OrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final CustomerService customerService;
    private final DoctorService doctorService;
    private final PackService packService;
    private final TimeSlotService timeSlotService;

    private final MailService mailService;

    public OrderService(
        OrderRepository orderRepository,
        OrderMapper orderMapper,
        CustomerService customerService,
        DoctorService doctorService,
        PackService packService,
        TimeSlotService timeSlotService,
        MailService mailService
    ) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.customerService = customerService;
        this.doctorService = doctorService;
        this.packService = packService;
        this.timeSlotService = timeSlotService;
        this.mailService = mailService;
    }

    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderDTO save(CreateOrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        OrderDTO dto = new OrderDTO();

        Order order = orderMapper.toEntity(dto);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Update a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderDTO update(OrderDTO orderDTO) {
        log.debug("Request to update Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Partially update a order.
     *
     * @param orderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderDTO> partialUpdate(OrderDTO orderDTO) {
        log.debug("Request to partially update Order : {}", orderDTO);

        return orderRepository
            .findById(orderDTO.getId())
            .map(existingOrder -> {
                orderMapper.partialUpdate(existingOrder, orderDTO);

                return existingOrder;
            })
            .map(orderRepository::save)
            .map(orderMapper::toDto);
    }

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderDTO> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id).map(orderMapper::toDto);
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }

    public void approveOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(OrderStatus.APPROVED);
        orderRepository.save(order);
        mailService.sendMailApproveOrder(order);
    }

    public void rejectOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(OrderStatus.REJECTED);
        orderRepository.save(order);
        mailService.sendMailRejectOrder(order);
    }

    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        mailService.sendMailCancelOrder(order);
    }

    public void completeOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(OrderStatus.COMPLETE);
        orderRepository.save(order);
        mailService.sendMailComplete(order);
    }
}
