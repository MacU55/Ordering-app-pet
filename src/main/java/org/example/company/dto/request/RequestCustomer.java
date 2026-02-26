package org.example.company.dto.request;

import org.example.company.model.Customer;
import org.example.company.service.validation.Mandatory;

public record RequestCustomer(
    @Mandatory String firstName,
    @Mandatory String lastName,
    @Mandatory String userName,
    @Mandatory String email) {
    public static Customer fromRequestCustomer(RequestCustomer requestCustomer) {
        return new Customer(requestCustomer.firstName(), requestCustomer.lastName(), requestCustomer.userName(),
            requestCustomer.email());
    }
}
