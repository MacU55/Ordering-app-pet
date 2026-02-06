package org.example.company.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.company.models.Department;
import org.example.company.models.Employee;

public record RequestEmployee(
    @NotNull(message = "name is mandatory") String name,
    @NotNull(message = "salary is mandatory") Double salary,
    @NotNull(message = "email is mandatory") String email,
    @NotNull(message = "department number is mandatory") int department) {

    public static Employee fromRequestEmployee(RequestEmployee requestEmployee) {
        return new Employee(requestEmployee.name, requestEmployee.salary, requestEmployee.email,
            Department.getByCode(requestEmployee.department));
    }
}
