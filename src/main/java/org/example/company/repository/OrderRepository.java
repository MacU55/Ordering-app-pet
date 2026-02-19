package org.example.company.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.example.company.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderDateCreatedAfter(LocalDateTime date);

    List<Order> findByOrderDateCreatedBetween(LocalDateTime start, LocalDateTime end);

    List<Order> findByOrderDateDeliveredIsNull();

    @Modifying
    @Query(
        value = """
            UPDATE orders
            SET order_date_delivered = :currentDateTime
            WHERE id = :orderId   
            """,
        nativeQuery = true

    )
    void updateOrderDateDeliveredByOrderId(
        @Param("orderId") Long orderId,
        @Param("currentDateTime") LocalDateTime currentDateTime);
}
