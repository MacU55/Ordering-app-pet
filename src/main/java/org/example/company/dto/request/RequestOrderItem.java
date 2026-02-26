package org.example.company.dto.request;

import jakarta.validation.constraints.Min;
import org.example.company.service.validation.Mandatory;

public record RequestOrderItem(
    @Mandatory Long itemId,
    @Mandatory Long orderId,
    @Min(value = 1, message = "quantity should be greater than 0") int quantity

//    BigDecimal price
) {
}
