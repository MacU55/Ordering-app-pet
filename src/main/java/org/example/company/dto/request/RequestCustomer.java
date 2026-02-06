package org.example.company.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.company.models.Customer;

public record RequestCustomer(
    @NotNull(message = "firstName is mandatory") String firstName,
    @NotNull(message = "lastName is mandatory") String lastName,
    @NotNull(message = "userName is mandatory") String userName,
    @NotNull(message = "email is mandatory") String email) {
    public static Customer fromRequestCustomer(RequestCustomer requestCustomer) {
        return new Customer(requestCustomer.firstName(), requestCustomer.lastName(), requestCustomer.userName(),
            requestCustomer.email());
    }
}
