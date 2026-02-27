package org.example.company.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.company.dto.request.RequestCustomer;
import org.example.company.dto.response.ResponseCustomer;
import org.example.company.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Customer management API")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/by-email")
    @Operation(summary = "Get customer by email")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name())")
    public ResponseEntity<ResponseCustomer> getCustomerByEmail(
        @Parameter(description = "Customer email", required = true) @RequestParam String email) {
        return customerService.findCustomerByEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-username")
    @Operation(summary = "Get customer by username")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name())")
    public ResponseEntity<ResponseCustomer> getCustomerByUserName(
        @Parameter(description = "Customer username", required = true) @RequestParam String userName) {
        return customerService.findCustomerByUserName(userName)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email-exists")
    @Operation(summary = "Check if email exists")
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name()," +
        "T(org.example.company.security.model.RoleTypes).CUSTOMER.name())")
    public Map<String, Boolean> checkEmailExists(
        @Parameter(description = "Email to check", required = true) @RequestParam String email) {
        return Map.of("exists", customerService.existsByEmail(email));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name()," +
        "T(org.example.company.security.model.RoleTypes).CUSTOMER.name())")
    public ResponseCustomer getCustomer(
        @Parameter(description = "Customer ID") @PathVariable Long id) {
        return customerService.findCustomerById(id);
    }

    @GetMapping
    @Operation(summary = "Get all customers")
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name())")
    public List<ResponseCustomer> getAllCustomers() {
        return customerService.findAllCustomers();
    }

    @PostMapping
    @Operation(summary = "Create customer",
        description = "Creates a new customer. Requires EMPLOYEE_STORE or CUSTOMER role.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Customer created successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).CUSTOMER.name())")
    public ResponseEntity<ResponseCustomer> createCustomer(
        @Parameter(description = "Customer data") @Valid @RequestBody RequestCustomer requestCustomer) {
        ResponseCustomer customer = customerService.createCustomer(requestCustomer);
        URI location = URI.create("/customers/" + customer.id());
        return ResponseEntity.created(location).body(customer);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customer updated"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PreAuthorize("hasAnyRole(" +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_STORE.name()," +
        "T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name()," +
        "T(org.example.company.security.model.RoleTypes).CUSTOMER.name())")
    public ResponseCustomer updateCustomer(
        @Parameter(description = "Customer ID") @PathVariable Long id,
        @Parameter(description = "Updated customer data") @Valid @RequestBody RequestCustomer requestCustomer) {
        return customerService.updateCustomer(id, requestCustomer);
    }
}
