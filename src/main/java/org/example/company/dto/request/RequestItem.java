package org.example.company.dto.request;

import java.math.BigDecimal;
import org.example.company.models.ItemType;

public record RequestItem(
    String name,
    String description,
    BigDecimal price,
    ItemType itemType) {
}
