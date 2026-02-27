package org.example.company.controller;

import java.net.URI;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.company.dto.request.RequestEmployee;
import org.example.company.dto.response.ResponseEmployee;
import org.example.company.model.Employee;
import org.example.company.service.EmployeeService;
import org.example.company.service.utility.converter.EmployeeConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "Employee management API")
@PreAuthorize("hasRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name())")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeConverter employeeConverter;

    @GetMapping("/by-name")
    @Operation(summary = "Get employee by name")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<ResponseEmployee> getEmployeeByName(
        @Parameter(description = "Employee name") @RequestParam String name) {
        return employeeService.findEmployeeByName(name)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email")
    @Operation(summary = "Get employee by email")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<ResponseEmployee> getEmployeeByEmail(
        @Parameter(description = "Employee email") @RequestParam String email) {
        return employeeService.findEmployeeByEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEmployee getEmployeeById(
        @Parameter(description = "Employee ID") @PathVariable long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return employeeConverter.convertToDTO(employee);
    }

    @GetMapping
    @Operation(summary = "Get all employees")
    public List<ResponseEmployee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping
    @Operation(summary = "Create employee",
        description = "Requires EMPLOYEE_ADMIN or SUPER_ADMIN role.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Employee created successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PreAuthorize("hasAnyRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name()," +
        "T(org.example.company.security.model.RoleTypes).SUPER_ADMIN.name())")
    public ResponseEntity<ResponseEmployee> createEmployee(
        @Parameter(description = "Employee data") @Valid @RequestBody RequestEmployee employee) {
        ResponseEmployee responseEmployee = employeeService.saveEmployee(employee);
        URI location = URI.create("/employees/" + responseEmployee.uuid());
        return ResponseEntity.created(location).body(responseEmployee);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee updated"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEmployee updateEmployee(
        @Parameter(description = "Employee ID") @PathVariable long id,
        @Parameter(description = "Updated employee data") @Valid @RequestBody RequestEmployee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Employee deleted"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<Void> deleteEmployee(
        @Parameter(description = "Employee ID") @PathVariable long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
