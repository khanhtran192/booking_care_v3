package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Doctor;
import com.mycompany.myapp.domain.Pack;
import com.mycompany.myapp.domain.TimeSlot;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TimeSlot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findAllByDoctorAndActiveIsTrue(Doctor doctor);
    List<TimeSlot> findAllByDoctor(Doctor doctor);
    List<TimeSlot> findAllByPackAndActiveIsTrue(Pack pack);
    List<TimeSlot> findAllByPack(Pack pack);
}
