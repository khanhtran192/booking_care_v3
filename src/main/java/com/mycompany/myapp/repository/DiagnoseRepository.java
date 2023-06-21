package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Diagnose;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Diagnose entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {}
