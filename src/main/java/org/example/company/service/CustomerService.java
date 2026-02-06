package org.example.company.service;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.company.dto.request.RequestCustomer;
import org.example.company.dto.response.ResponseCustomer;
import org.example.company.exceptions.BaseException;
import org.example.company.exceptions.EmailAlreadyExistsException;
import org.example.company.models.Customer;
import org.example.company.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public Optional<ResponseCustomer> findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).map(ResponseCustomer::fromCustomer);
    }

    @Transactional(readOnly = true)
    public Optional<ResponseCustomer> findCustomerByUserName(String userName) {
        return customerRepository.findByUserName(userName).map(ResponseCustomer::fromCustomer);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public ResponseCustomer findCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found for id= " + id));
        return ResponseCustomer.fromCustomer(customer);
    }

    @Transactional(readOnly = true)
    public List<ResponseCustomer> findAllCustomers() {
        return customerRepository.findAll().stream().map(ResponseCustomer::fromCustomer).toList();
    }

    @Transactional
    public ResponseCustomer createCustomer(RequestCustomer requestCustomer) {
        if (customerRepository.existsByEmail(requestCustomer.email())) {
            log.error("Customer with email: {} already exists", requestCustomer.email());
            throw new EmailAlreadyExistsException(BaseException.ErrorType.EMAIL_ALREADY_EXISTS, requestCustomer.email());
        } else {
            Customer customer = customerRepository.save(RequestCustomer.fromRequestCustomer(requestCustomer));
            return ResponseCustomer.fromCustomer(customer);
        }
    }

    @Transactional
    public ResponseCustomer updateCustomer(long customerId, RequestCustomer requestCustomer) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found for id= " + customerId));
         customer.updateCustomer(requestCustomer);
        return ResponseCustomer.fromCustomer(customer);
    }
}
