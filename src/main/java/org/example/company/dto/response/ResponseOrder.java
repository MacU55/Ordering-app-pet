package org.example.company.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import org.example.company.models.Order;

public record ResponseOrder(
    long id,
    LocalDateTime orderDateCreated,
    LocalDateTime orderDateDelivered,
    List<ResponseOrderItem> items,
    long customerId
) {
    public static ResponseOrder fromOrder(Order order) {
        return new ResponseOrder(
            order.getId(),
            order.getOrderDateCreated(),
            order.getOrderDateDelivered(),
            order.getOrderItemSet().stream()
                .map(ResponseOrderItem::fromOrderItem)
                .toList(),
            order.getCustomer().getId()
        );
    }
}
