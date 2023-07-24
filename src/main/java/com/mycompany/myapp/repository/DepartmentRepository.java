package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.domain.Hospital;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByHospital(Hospital hospital);

    Page<Department> findAllByActiveIsTrue(Pageable pageable);
}
