package org.example.company.dto.response;

import java.math.BigDecimal;

public record ResponseOrderItem(
    long id,
    long orderId,
    long itemId,
    String itemName,
    int quantity,
    BigDecimal priceAtOrderTime
) {
}
