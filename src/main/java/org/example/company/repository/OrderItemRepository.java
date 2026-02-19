package org.example.company.repository;

import java.util.List;
import org.example.company.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder_Id(Long orderId);

    boolean existsByOrder_IdAndItem_Id(Long orderId, Long itemId);
}
