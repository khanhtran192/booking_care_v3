package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.domain.enumeration.OrderStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.pack = :pack AND o.status IN (:status)")
    List<Order> orderByPack(@Param("pack") Pack pack, @Param("status") List<OrderStatus> status);

    @Query("SELECT o FROM Order o WHERE o.doctor = :doctor AND o.status IN (:status)")
    List<Order> orderByDoctor(@Param("doctor") Doctor doctor, @Param("status") List<OrderStatus> status);

    @Query("SELECT o FROM Order o WHERE (o.doctor = :doctor) AND (:status IS NULL OR o.status = :status)  ORDER BY o.date DESC")
    Page<Order> findAllbyDoctor(@Param("doctor") Doctor doctor, @Param("status") OrderStatus status, Pageable pageable);

    List<Order> findAllByDoctorAndDateAndTimeslotAndStatusIn(Doctor doctor, LocalDate date, TimeSlot timeSlot, List<OrderStatus> status);
    List<Order> findAllByPackAndDateAndTimeslotAndStatusIn(Pack pack, LocalDate date, TimeSlot timeSlot, List<OrderStatus> status);

    //    List<Order> findAllByCustomer(Customer customer);
    Page<Order> findAllByCustomer(Customer customer, Pageable pageable);

    List<Order> findAllByPackOrderByDateDesc(Pack pack);
    List<Order> findAllByDoctor(Doctor doctor);

    List<Order> findAllByDateIsBeforeAndStatus(LocalDate date, OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.hospitalId = :hospitalId ORDER BY o.status DESC, o.date DESC")
    Page<Order> findAllByHospitalIdOrderByDateDesc(@Param("hospitalId") Long hospitalId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE (o.hospitalId = :hospitalId) AND (:status IS NULL OR o.status = :status) ORDER BY o.date DESC")
    Page<Order> findAllByHospitalIdOrderByDateDescV2(
        @Param("hospitalId") Long hospitalId,
        @Param("status") OrderStatus status,
        Pageable pageable
    );
}
