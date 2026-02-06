package org.example.company.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.company.dto.request.RequestCustomer;
import org.example.company.dto.response.ResponseCustomer;
import org.example.company.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.example.company.security.annotation.IsAllowedByRole;
import org.example.company.security.model.Roles;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/by-email")
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.EMPLOYEE_ADMINISTRATION})
    public ResponseEntity<ResponseCustomer> getCustomerByEmail(@RequestParam String email) {
        return customerService.findCustomerByEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-username")
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.EMPLOYEE_ADMINISTRATION})
    public ResponseEntity<ResponseCustomer> getCustomerByUserName(@RequestParam String userName) {
        return customerService.findCustomerByUserName(userName)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email-exists")
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.EMPLOYEE_ADMINISTRATION, Roles.CUSTOMER})
    public Map<String, Boolean> checkEmailExists(@RequestParam String email) {
        return Map.of("exists", customerService.existsByEmail(email));
    }

    @GetMapping("/{id}")
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.EMPLOYEE_ADMINISTRATION, Roles.CUSTOMER})
    public ResponseCustomer getCustomer(@PathVariable Long id) {
        return customerService.findCustomerById(id);
    }

    @GetMapping
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.EMPLOYEE_ADMINISTRATION})
    public List<ResponseCustomer> getAllCustomers() {
        return customerService.findAllCustomers();
    }

    @PostMapping
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.CUSTOMER})
    public ResponseEntity<ResponseCustomer> createCustomer(@Valid @RequestBody RequestCustomer requestCustomer) {
        ResponseCustomer customer = customerService.createCustomer(requestCustomer);
        URI location = URI.create("/customers/" + customer.id());
        return ResponseEntity.created(location).body(customer);
    }

    @PutMapping("/{id}")
    @IsAllowedByRole({Roles.EMPLOYEE_STORE, Roles.EMPLOYEE_ADMINISTRATION, Roles.CUSTOMER})
    public ResponseCustomer updateCustomer(@PathVariable Long id, @Valid @RequestBody RequestCustomer requestCustomer) {
        return customerService.updateCustomer(id, requestCustomer);
    }
}
