package org.example.company.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RequestOrderItemCreate(
    @NotNull Long orderId,
    @NotNull Long itemId,
    @Min(value = 1, message = "quantity should be greater than 0") int quantity,
    BigDecimal price
) {
}
