package org.example.company.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.example.company.service.validation.Mandatory;

public record RequestOrderItemCreate(
    @NotNull Long orderId,
    @NotNull Long itemId,
    @Min(value = 1, message = "quantity should be greater than 0") int quantity
) {
}
