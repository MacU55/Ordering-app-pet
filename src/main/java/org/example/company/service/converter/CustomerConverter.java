package org.example.company.service.converter;

import org.example.company.dto.request.RequestCustomer;
import org.example.company.dto.response.ResponseCustomer;
import org.example.company.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerConverter extends ConverterImpl<Customer, ResponseCustomer, RequestCustomer> {

    public CustomerConverter() {
    }

    @Override
    public ResponseCustomer convertToDTO(Customer customer) {
        return new ResponseCustomer(customer.getId(), customer.getFirstName(), customer.getLastName(),
            customer.getUserName());
    }

    @Override
    public Customer convertToEntity(RequestCustomer requestCustomer) {
        return new Customer(requestCustomer.firstName(), requestCustomer.lastName(),
            requestCustomer.userName(), requestCustomer.email());
    }

}
