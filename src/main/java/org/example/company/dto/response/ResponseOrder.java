package org.example.company.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseOrder(
    long id,
    LocalDateTime orderDateCreated,
    LocalDateTime orderDateDelivered,
    List<ResponseOrderItem> items,
    long customerId
) {
}
