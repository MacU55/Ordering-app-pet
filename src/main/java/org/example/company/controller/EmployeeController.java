package org.example.company.controller;

import java.net.URI;
import java.util.List;
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
@PreAuthorize("hasRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name())")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeConverter employeeConverter;

    @GetMapping("/by-name")
    public ResponseEntity<ResponseEmployee> getEmployeeByName(@RequestParam String name) {
        return employeeService.findEmployeeByName(name)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email")
    public ResponseEntity<ResponseEmployee> getEmployeeByEmail(@RequestParam String email) {
        return employeeService.findEmployeeByEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping("/by-roleType")
//    public List<ResponseEmployee> getEmployeesByDepartment(@RequestParam RoleTypes departmentRole) {
//        return employeeService.findEmployeesByDepartment(departmentRole);
//    }

    @GetMapping("/{id}")
    public ResponseEmployee getEmployeeById(@PathVariable long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return employeeConverter.convertToDTO(employee);
    }

    @GetMapping
    public List<ResponseEmployee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

//    @GetMapping("/{id}/roleType")
//    @IsAllowedByRole({RoleTypes.EMPLOYEE_ADMIN})
//    public Set<RoleTypes> getDepartmentInfo(@PathVariable long id) {
//        return employeeService.getDepartmentInfoByEmployeeId(id);
//    }

    @PostMapping
    @PreAuthorize("hasAnyRole(T(org.example.company.security.model.RoleTypes).EMPLOYEE_ADMIN.name()," +
        "T(org.example.company.security.model.RoleTypes).SUPER_ADMIN.name())")
    public ResponseEntity<ResponseEmployee> createEmployee(@Valid @RequestBody RequestEmployee employee) {
        ResponseEmployee responseEmployee = employeeService.saveEmployee(employee);
        URI location = URI.create("/employees/" + responseEmployee.uuid());
        return ResponseEntity.created(location).body(responseEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEmployee updateEmployee(@PathVariable long id, @Valid @RequestBody RequestEmployee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
