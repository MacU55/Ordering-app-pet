package org.example.company.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.company.service.validation.Mandatory;

public record RequestEmployee(
    @Mandatory String name,
    @NotNull Double salary,
     int roleType
) {
}
