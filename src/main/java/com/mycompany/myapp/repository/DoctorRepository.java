package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Department;
import com.mycompany.myapp.domain.Doctor;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Doctor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllByDepartment(Department department);
    Doctor findDoctorByUserId(Long userId);

    @Query(
        value = "SELECT d FROM Doctor d " +
        "JOIN Department dp ON d.department.id = dp.id " +
        "WHERE d.hospitalId = :hospitalId AND " +
        "(" +
        "   (:keyword IS NULL OR d.name LIKE %:keyword%) OR " +
        "   (:keyword IS NULL OR d.specialize LIKE %:keyword%) OR " +
        "   (:keyword IS NULL OR d.degree LIKE %:keyword%) OR " +
        "   (:keyword IS NULL OR dp.departmentName LIKE %:keyword%)" +
        ") " +
        "GROUP BY d.id"
    )
    Page<Doctor> pageDoctorByHospital(Pageable pageable, @Param("hospitalId") Integer hospitalId, @Param("keyword") String keyword);

    @Query(
        value = "SELECT d FROM Doctor d " +
        "JOIN Department dp ON d.department.id = dp.id " +
        "WHERE d.hospitalId = :hospitalId AND d.active = TRUE AND " +
        "(" +
        "   (:keyword IS NULL OR d.name LIKE %:keyword%) OR " +
        "   (:keyword IS NULL OR d.specialize LIKE %:keyword%) OR " +
        "   (:keyword IS NULL OR d.degree LIKE %:keyword%) OR " +
        "   (:keyword IS NULL OR dp.departmentName LIKE %:keyword%)" +
        ") " +
        "GROUP BY d.id"
    )
    Page<Doctor> pageDoctorByHospitalForUser(Pageable pageable, @Param("hospitalId") Integer hospitalId, @Param("keyword") String keyword);

    @Query(
        value = "SELECT d FROM Doctor d " +
        "JOIN Department dp ON d.department.id = dp.id " +
        "WHERE d.active = TRUE AND " +
        "(" +
        "   (:keyword IS NULL OR d.name LIKE %:keyword%) OR " +
        "   (:keyword IS NULL OR d.specialize LIKE %:keyword%) OR " +
        "   (:keyword IS NULL OR d.degree LIKE %:keyword%) OR " +
        "   (:keyword IS NULL OR dp.departmentName LIKE %:keyword%)" +
        ") " +
        "GROUP BY d.id"
    )
    Page<Doctor> pageDoctorForUser(Pageable pageable, @Param("keyword") String keyword);
}
