package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Diagnose;
import com.mycompany.myapp.domain.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Diagnose entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {
    Optional<Diagnose> findByOrder(Order order);
}
