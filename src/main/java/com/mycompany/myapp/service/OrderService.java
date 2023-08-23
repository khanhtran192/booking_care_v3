package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.enumeration.OrderStatus;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.*;
import com.mycompany.myapp.service.dto.OrderDTO;
import com.mycompany.myapp.service.dto.request.CreateOrderDTO;
import com.mycompany.myapp.service.dto.response.OrderResponseDTO;
import com.mycompany.myapp.service.mapper.OrderMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final HospitalRepository hospitalRepository;

    private final TimeSlotRepository timeSlotRepository;

    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final DoctorRepository doctorRepository;

    private final MailService mailService;
    private final MapperService mapperService;
    private final PackRepository packRepository;

    public OrderService(
        OrderRepository orderRepository,
        OrderMapper orderMapper,
        HospitalRepository hospitalRepository,
        CustomerService customerService,
        DoctorService doctorService,
        PackService packService,
        TimeSlotService timeSlotService,
        TimeSlotRepository timeSlotRepository,
        CustomerRepository customerRepository,
        UserService userService,
        DoctorRepository doctorRepository,
        MailService mailService,
        MapperService mapperService,
        PackRepository packRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.hospitalRepository = hospitalRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.customerRepository = customerRepository;
        this.userService = userService;
        this.doctorRepository = doctorRepository;
        this.mailService = mailService;
        this.mapperService = mapperService;
        this.packRepository = packRepository;
    }

    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderDTO save(CreateOrderDTO orderDTO) {
        return null;
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
    public Optional<OrderResponseDTO> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id).map(mapperService::mapToDto);
    }

    public void approveOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(OrderStatus.APPROVED);
        orderRepository.save(order);
        mailService.sendMailApproveOrder(order);
        if (order.getDoctor() != null) {
            List<Order> orders = orderRepository.findAllByDoctorAndDateAndTimeslotAndStatusIn(
                order.getDoctor(),
                order.getDate(),
                order.getTimeslot(),
                List.of(OrderStatus.PENDING)
            );
            for (Order o : orders) {
                rejectOrder(o.getId());
            }
        } else if (order.getPack() != null) {
            List<Order> orders = orderRepository.findAllByPackAndDateAndTimeslotAndStatusIn(
                order.getPack(),
                order.getDate(),
                order.getTimeslot(),
                List.of(OrderStatus.PENDING)
            );
            for (Order o : orders) {
                rejectOrder(o.getId());
            }
        }
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

    public OrderResponseDTO createOrderDoctor(Long id, CreateOrderDTO create) {
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new NotFoundException("user not found"));
        Customer customer = customerRepository.findByUserBooking(user.getId());
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor not found"));
        TimeSlot timeSlot = timeSlotRepository
            .findById(create.getTimeSlot())
            .orElseThrow(() -> new NotFoundException("Time slot not found"));
        Hospital hospital = hospitalRepository
            .findById(Long.valueOf(doctor.getHospitalId()))
            .orElseThrow(() -> new NotFoundException("Hospital not found"));
        Order order = new Order();
        order.setDoctor(doctor);
        order.setCustomer(customer);
        order.setAddress(hospital.getAddress());
        order.setHospitalId(hospital.getId());
        order.setTimeslot(timeSlot);
        order.setPrice(timeSlot.getPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setDate(create.getDate());
        order.setSymptom(create.getSymptom());
        order = orderRepository.save(order);
        mailService.sendMailHaveNewOrder(order);
        return mapperService.mapToDto(order);
    }

    public OrderResponseDTO createOrderPack(Long id, CreateOrderDTO create) {
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new NotFoundException("user not found"));
        Customer customer = customerRepository.findByUserBooking(user.getId());
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }
        Pack pack = packRepository.findById(id).orElseThrow(() -> new NotFoundException("Doctor not found"));
        TimeSlot timeSlot = timeSlotRepository
            .findById(create.getTimeSlot())
            .orElseThrow(() -> new NotFoundException("Time slot not found"));
        Hospital hospital = pack.getHospital();
        Order order = new Order();
        order.setPack(pack);
        order.setCustomer(customer);
        order.setAddress(hospital.getAddress());
        order.setHospitalId(hospital.getId());
        order.setTimeslot(timeSlot);
        order.setPrice(timeSlot.getPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setDate(create.getDate());
        order.setSymptom(create.getSymptom());
        order = orderRepository.save(order);
        mailService.sendMailHaveNewOrder(order);
        return mapperService.mapToDto(order);
    }

    public List<OrderResponseDTO> listOrderPersonal() {
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new NotFoundException("user not found"));
        Customer customer = customerRepository.findByUserBooking(user.getId());
        if (customer == null) {
            throw new NotFoundException("Customer not found");
        }
        List<Order> orders = orderRepository.findAllByCustomer(customer);
        return orders.stream().map(mapperService::mapToDto).collect(Collectors.toList());
    }

    public OrderResponseDTO updateOrder(Long id, CreateOrderDTO create) {
        TimeSlot timeSlot = timeSlotRepository
            .findById(create.getTimeSlot())
            .orElseThrow(() -> new NotFoundException("Time slot not found"));
        Order order = orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        order.setTimeslot(timeSlot);
        order.setPrice(timeSlot.getPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setDate(create.getDate());
        order.setSymptom(create.getSymptom());
        order = orderRepository.save(order);
        mailService.sendMailChangeOrder(order);
        return mapperService.mapToDto(order);
    }

    public List<OrderResponseDTO> listOrderByPack(Long id) {
        Pack pack = packRepository.findById(id).orElseThrow(() -> new NotFoundException("pack not found"));
        List<Order> orders = orderRepository.findAllByPackOrderByDateDesc(pack);
        return orders.stream().map(mapperService::mapToDto).collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void rejectOrderOutDate() {
        LocalDate currentDate = LocalDate.now();
        List<Order> orders = orderRepository.findAllByDateIsBeforeAndStatus(currentDate, OrderStatus.PENDING);
        for (Order order : orders) {
            rejectOrder(order.getId());
        }
    }
}
