package org.example.company.dto.request;

import jakarta.validation.constraints.Min;
import org.example.company.service.validation.Mandatory;

public record RequestOrderItemCreate(
    @Mandatory Long orderId,
    @Mandatory Long itemId,
    @Min(value = 1, message = "quantity should be greater than 0") int quantity
) {
}
