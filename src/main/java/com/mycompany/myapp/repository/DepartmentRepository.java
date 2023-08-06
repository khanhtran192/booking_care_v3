package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.domain.Hospital;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByHospital(Hospital hospital);

    @Query(
        value = "SELECT d FROM Department d " +
        "JOIN Doctor dt ON dt.department.id = d.id " +
        "WHERE d.hospital = :hospital AND " +
        "((:keyword IS NULL OR d.departmentName LIKE %:keyword%) OR " +
        "(:keyword IS NULL OR dt.name LIKE %:keyword%)) " +
        "GROUP BY d.id"
    )
    Page<Department> pageDepartmentByHospital(Pageable pageable, @Param("hospital") Hospital hospital, @Param("keyword") String keyword);

    @Query(
        value = "SELECT d FROM Department d " +
        "JOIN Doctor dt ON dt.department.id = d.id " +
        "JOIN Hospital h ON d.hospital.id = h.id " +
        "WHERE d.active = TRUE AND " +
        "((:keyword IS NULL OR d.departmentName LIKE %:keyword%) OR " +
        "(:keyword IS NULL OR dt.name LIKE %:keyword%) OR " +
        "(:keyword IS NULL OR h.name LIKE %:keyword%)) " +
        "GROUP BY d.id"
    )
    Page<Department> pageDepartmentForUser(Pageable pageable, @Param("keyword") String keyword);

    @Query(
        value = "SELECT d FROM Department d " +
        "JOIN Doctor dt ON dt.department.id = d.id " +
        "WHERE d.hospital = :hospital AND d.active = TRUE AND " +
        "((:keyword IS NULL OR d.departmentName LIKE %:keyword%) OR " +
        "(:keyword IS NULL OR dt.name LIKE %:keyword%)) " +
        "GROUP BY d.id"
    )
    Page<Department> pageDepartmentByHospitalForUser(
        Pageable pageable,
        @Param("hospital") Hospital hospital,
        @Param("keyword") String keyword
    );
}
