package org.example.company.dto.response;

import org.example.company.models.Employee;

public record ResponseEmployee(
    long id,
    String name,
    String email
) {
    public static ResponseEmployee fromEmployee(Employee employee) {
        return new ResponseEmployee(employee.getId(), employee.getName(), employee.getEmail());
    }
}
