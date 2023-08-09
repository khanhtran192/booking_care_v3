package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Hospital;
import com.mycompany.myapp.domain.Pack;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pack entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackRepository extends JpaRepository<Pack, Long> {
    Boolean existsByNameAndHospital(String name, Hospital hospital);

    @Query(
        value = "SELECT p FROM Pack p " +
        "JOIN Hospital h ON h.id = p.hospital.id " +
        "WHERE (:active IS NULL OR p.active = :active) AND " +
        "(:hospital IS NULL OR p.hospital = :hospital) AND " +
        "(" +
        "(:keyword IS NULL OR p.name LIKE %:keyword%) OR " +
        "(:keyword IS NULL OR h.name LIKE %:keyword%) " +
        ") GROUP BY p.id"
    )
    Page<Pack> pagePack(
        Pageable pageable,
        @Param("active") Boolean active,
        @Param("hospital") Hospital hospital,
        @Param("keyword") String keyword
    );
}
