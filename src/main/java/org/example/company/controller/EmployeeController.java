package org.example.company.controller;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.company.dto.request.RequestEmployee;
import org.example.company.dto.response.ResponseEmployee;
import org.example.company.models.Department;
import org.example.company.models.Employee;
import org.example.company.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/by-name")
    @IsAllowedByRole({Roles.EMPLOYEE_ADMINISTRATION})
    public ResponseEntity<ResponseEmployee> getEmployeeByName(@RequestParam String name) {
        return employeeService.findEmployeeByName(name)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email")
    @IsAllowedByRole({Roles.EMPLOYEE_ADMINISTRATION})
    public ResponseEntity<ResponseEmployee> getEmployeeByEmail(@RequestParam String email) {
        return employeeService.findEmployeeByEmail(email)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-department")
    @IsAllowedByRole({Roles.EMPLOYEE_ADMINISTRATION})
    public List<ResponseEmployee> getEmployeesByDepartment(@RequestParam Department department) {
        return employeeService.findEmployeesByDepartment(department);
    }

    @GetMapping("/{id}")
    @IsAllowedByRole({Roles.EMPLOYEE_ADMINISTRATION})
    public ResponseEmployee getEmployeeById(@PathVariable long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEmployee.fromEmployee(employee);
    }

    @GetMapping
    @IsAllowedByRole({Roles.EMPLOYEE_ADMINISTRATION})
    public List<ResponseEmployee> getAllEmployees() {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        return allEmployees.stream().map(ResponseEmployee::fromEmployee).toList();
    }

    @GetMapping("/{id}/department")
    @IsAllowedByRole({Roles.EMPLOYEE_ADMINISTRATION})
    public Department.DepartmentInfo getDepartmentInfo(@PathVariable long id) {
        return employeeService.getDepartmentInfoByEmployeeId(id);
    }

    @PostMapping
    @IsAllowedByRole({Roles.EMPLOYEE_ADMINISTRATION})
    public ResponseEntity<ResponseEmployee> createEmployee(@Valid @RequestBody RequestEmployee employee) {
        ResponseEmployee responseEmployee = employeeService.saveEmployee(employee);
        URI location = URI.create("/employees/" + responseEmployee.id());
        return ResponseEntity.created(location).body(responseEmployee);
    }

    @PutMapping("/{id}")
    @IsAllowedByRole({Roles.EMPLOYEE_ADMINISTRATION})
    public ResponseEmployee updateEmployee(@PathVariable long id, @Valid @RequestBody RequestEmployee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    @IsAllowedByRole({Roles.EMPLOYEE_ADMINISTRATION})
    public ResponseEntity<Void> deleteEmployee(@PathVariable long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
