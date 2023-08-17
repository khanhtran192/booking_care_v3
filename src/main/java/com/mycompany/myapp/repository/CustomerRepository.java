package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Customer;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUserBooking(Long userBooking);

    @Query(
        "SELECT c FROM Customer c " +
        "JOIN Order o ON c.id = o.customer.id " +
        "JOIN Hospital h ON h.id = o.hospitalId " +
        "WHERE h.id = :id " +
        "ORDER BY c.id"
    )
    Page<Customer> customerByHospital(@Param("id") Long id, Pageable page);
}
