package org.example.company.dto.response;

public record ResponseCustomer(
    long id,
    String firstName,
    String lastName,
    String username
) {
}
