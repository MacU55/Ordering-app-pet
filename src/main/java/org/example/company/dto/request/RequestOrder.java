package org.example.company.dto.request;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record RequestOrder(
    @NotEmpty(message = "list of orderItems shouldn't be empty") List<@Valid RequestOrderItem> items, long customerId
) {
}
