package org.example.company.dto.response;

import java.math.BigDecimal;
import org.example.company.models.Item;

public record ResponseItem(
    long id,
    String name,
    BigDecimal price,
    String description
) {
    public static ResponseItem fromItem(Item item) {
        return new ResponseItem(item.getId(), item.getName(), item.getPrice(), item.getDescription());
    }
}
