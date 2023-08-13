package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Hospital entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Boolean existsByEmail(String email);

    @Query(
        value = "SELECT h FROM Hospital h " +
        "JOIN Department d ON h.id = d.hospital.id " +
        "JOIN Doctor dt ON h.id = dt.hospitalId " +
        "JOIN Pack p ON h.id = p.hospital.id " +
        "WHERE h.active = TRUE AND " +
        "(((:keyword) IS NULL OR h.name LIKE %:keyword%) OR " +
        "((:keyword) IS NULL OR h.address LIKE %:keyword%) OR " +
        "((:keyword) IS NULL OR d.departmentName LIKE %:keyword%) OR " +
        "((:keyword) IS NULL OR dt.name LIKE %:keyword%) OR " +
        "((:keyword) IS NULL OR dt.specialize LIKE %:keyword%) OR " +
        "((:keyword) IS NULL OR p.name LIKE %:keyword%)) " +
        "GROUP BY h.id"
    )
    Page<Hospital> listHospitalForUser(Pageable pageable, @Param("keyword") String keyword);

    @Query(
        value = "SELECT h FROM Hospital h " +
        "JOIN Department d ON h.id = d.hospital.id " +
        "JOIN Doctor dt ON h.id = dt.hospitalId " +
        "JOIN Pack p ON h.id = p.hospital.id " +
        "WHERE " +
        "(((:keyword) IS NULL OR h.name LIKE %:keyword%) OR " +
        "((:keyword) IS NULL OR h.address LIKE %:keyword%) OR " +
        "((:keyword) IS NULL OR d.departmentName LIKE %:keyword%) OR " +
        "((:keyword) IS NULL OR dt.name LIKE %:keyword%) OR " +
        "((:keyword) IS NULL OR dt.specialize LIKE %:keyword%) OR " +
        "((:keyword) IS NULL OR p.name LIKE %:keyword%)) " +
        "GROUP BY h.id"
    )
    Page<Hospital> listHospital(Pageable pageable, @Param("keyword") String keyword);

    Hospital findHospitalByUserId(Long userId);
}
