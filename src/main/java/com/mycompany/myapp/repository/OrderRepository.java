package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.Pack;
import com.mycompany.myapp.domain.enumeration.OrderStatus;
import java.util.List;
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
}
