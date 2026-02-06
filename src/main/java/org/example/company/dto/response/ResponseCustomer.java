package org.example.company.dto.response;

import org.example.company.models.Customer;

public record ResponseCustomer(
    long id,
    String firstName,
    String lastName,
    String username
) {
    public static ResponseCustomer fromCustomer(Customer customer) {
        return new ResponseCustomer(
            customer.getId(),
            customer.getFirstName(),
            customer.getLastName(),
            customer.getUserName()
        );
    }
}
