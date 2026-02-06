package org.example.company.dto.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.Min;

public record RequestOrderItemUpdate(
    @Min(value = 1, message = "quantity should be greater than 0") int quantity,
    BigDecimal price
) {
}
