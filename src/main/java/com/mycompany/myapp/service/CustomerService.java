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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public CustomerDTO findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        try {
            return customerMapper.toDto(customerRepository.findByUserBooking(user.getId()));
        } catch (Exception e) {
            return null;
        }
    }

    public void createCustomer(CreateCustomerDTO dto) {
        Customer customer = mapperService.mapToEntity(dto);
        Map<String, String> name = getName(customer.getFullName());
        User user = userService.getUserWithAuthorities().orElseThrow(() -> new NotFoundException("User not found"));
        user.setFirstName(name.get("firstName"));
        user.setLastName(name.get("lastName"));
        userRepository.save(user);
        Customer customerCheck = customerRepository.findByUserBooking(user.getId());
        if (customerCheck != null) {
            throw new AlreadyExistedException("Customer already exists");
        }
        customer.setEmail(user.getEmail());
        customer.setUserBooking(user.getId());
        customer.setFirstName(name.get("firstName"));
        customer.setLastName(name.get("lastName"));
        customerRepository.save(customer);
    }

    public void updateCustomer(CreateCustomerDTO dto, Long id) {
        Map<String, String> name = getName(dto.getFullName());
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));
        customer.setFullName(dto.getFullName());
        customer.setFirstName(name.get("firstName"));
        customer.setLastName(name.get("lastName"));
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setGender(Gender.valueOf(dto.getGender()));
        customer.setAddress(dto.getAddress());
        customer.setIdCard(dto.getIdCard());
        customerRepository.save(customer);
        User user = userRepository.findById(customer.getUserBooking()).orElseThrow(() -> new NotFoundException("User not found"));
        user.setFirstName(name.get("firstName"));
        user.setLastName(name.get("lastName"));
        userRepository.save(user);
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

    public Map<String, String> getName(String fullname) {
        Map<String, String> result = new HashMap<String, String>();
        String[] parts = fullname.split(" ");

        // Kiểm tra xem có đủ 2 phần (firstname và lastname) không
        if (parts.length >= 2) {
            String firstName = parts[0];

            // Dùng StringBuilder để kết hợp các phần còn lại thành lastname
            StringBuilder lastNameBuilder = new StringBuilder();
            for (int i = 1; i < parts.length; i++) {
                lastNameBuilder.append(parts[i]);
                if (i < parts.length - 1) {
                    lastNameBuilder.append(" ");
                }
            }
            String lastName = lastNameBuilder.toString();
            result.put("lastName", lastName);
            result.put("firstName", firstName);
            return result;
        } else {
            result.put("lastName", "lastName");
            result.put("firstName", "firstName");
            return result;
        }
    }
}
