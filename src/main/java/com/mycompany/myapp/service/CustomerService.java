package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Customer;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.enumeration.Gender;
import com.mycompany.myapp.exception.AlreadyExistedException;
import com.mycompany.myapp.exception.NotFoundException;
import com.mycompany.myapp.repository.CustomerRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.dto.CustomerDTO;
import com.mycompany.myapp.service.dto.request.CreateCustomerDTO;
import com.mycompany.myapp.service.dto.response.CustomerResponseDTO;
import com.mycompany.myapp.service.mapper.CustomerMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Customer}.
 */
@Service
@Transactional
public class CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;
    private final MapperService mapperService;
    private final UserService userService;
    private final UserRepository userRepository;

    public CustomerService(
        CustomerRepository customerRepository,
        CustomerMapper customerMapper,
        MapperService mapperService,
        UserService userService,
        UserRepository userRepository
    ) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.mapperService = mapperService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<CustomerDTO> findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        return customerRepository.findById(id).map(customerMapper::toDto);
    }

    public void createCustomer(CreateCustomerDTO dto) {
        Customer customer = mapperService.mapToEntity(dto);
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new NotFoundException("User not found"));
        Customer customerCheck = customerRepository.findByUserBooking(user.getId());
        if (customerCheck != null) {
            throw new AlreadyExistedException("Customer already exists");
        }
        customer.setEmail(user.getEmail());
        customer.setUserBooking(user.getId());
        customerRepository.save(customer);
    }

    public void updateCustomer(CreateCustomerDTO dto, Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
        customer.setFullName(dto.getFullName());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setGender(Gender.valueOf(dto.getGender()));
        customer.setIdCard(dto.getIdCard());
        customerRepository.save(customer);
    }

    public CustomerResponseDTO findByUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        Customer customer = customerRepository.findByUserBooking(user.getId());
        if (customer == null) {
            return null;
        }
        return mapperService.mapToDto(customer);
    }

    public Page<CustomerResponseDTO> listCustomerByHospial(Long id, Pageable pageable) {
        Page<Customer> customers = customerRepository.customerByHospital(id, pageable);
        return customers.map(mapperService::mapToDto);
    }
}
