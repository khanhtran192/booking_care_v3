package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Hospital;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Hospital entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {}
