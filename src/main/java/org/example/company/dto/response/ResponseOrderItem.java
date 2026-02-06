package org.example.company.dto.response;

import java.math.BigDecimal;
import org.example.company.models.OrderItem;

public record ResponseOrderItem(
    long id,
    long orderId,
    long itemId,
    String itemName,
    int quantity,
    BigDecimal priceAtOrderTime
) {
    public static ResponseOrderItem fromOrderItem(OrderItem orderItem) {
        return new ResponseOrderItem(
            orderItem.getId(),
            orderItem.getOrder().getId(),
            orderItem.getItem().getId(),
            orderItem.getItem().getName(),
            orderItem.getQuantity(),
            orderItem.getPriceAtOrderTime()
        );
    }
}
