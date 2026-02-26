package org.example.company.dto.request;

import org.example.company.service.validation.Mandatory;

public record RequestEmployee(
    @Mandatory String name,
    @Mandatory Double salary,
     int roleType
) {
}
