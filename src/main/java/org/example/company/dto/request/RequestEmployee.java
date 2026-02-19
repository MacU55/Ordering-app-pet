package org.example.company.dto.request;

import jakarta.validation.constraints.NotNull;

public record RequestEmployee(
    @NotNull(message = "name is mandatory") String name,
    @NotNull(message = "salary is mandatory") Double salary
//    @NotNull(message = "department number is mandatory") int department
) {
}
