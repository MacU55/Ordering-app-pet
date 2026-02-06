package org.example.company.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.company.dto.request.RequestEmployee;
import org.example.company.dto.response.ResponseEmployee;
import org.example.company.models.Department;
import org.example.company.models.Employee;
import org.example.company.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public Optional<ResponseEmployee> findEmployeeByName(String name) {
        return employeeRepository.findByName(name).map(ResponseEmployee::fromEmployee);
    }

    @Transactional(readOnly = true)
    public Optional<ResponseEmployee> findEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email).map(ResponseEmployee::fromEmployee);
    }

    @Transactional(readOnly = true)
    public List<ResponseEmployee> findEmployeesByDepartment(Department department) {
        return employeeRepository.findByDepartment(department).stream()
            .map(ResponseEmployee::fromEmployee)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee with id " + id + " not found"));
    }

    @Transactional
    public ResponseEmployee saveEmployee(RequestEmployee requestEmployee) {
        Employee savedEmployee = employeeRepository.save(RequestEmployee.fromRequestEmployee(requestEmployee));
        return ResponseEmployee.fromEmployee(savedEmployee);
    }

    @Transactional
    public ResponseEmployee updateEmployee(long employeeId, RequestEmployee requestEmployee) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee with id " + employeeId + " not found"));
        employee.updateEmployee(requestEmployee);
        return ResponseEmployee.fromEmployee(employee);
    }

    @Transactional
    public void deleteEmployee(long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        } else {
            log.error("Employee with id {} not found", id);
            throw new EntityNotFoundException("Employee with id " + id + " not found");
        }
    }

    @Transactional(readOnly = true)
    public Department.DepartmentInfo getDepartmentInfoByEmployeeId(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee with id " + employeeId + " not found"));
        Department department = employee.getDepartment();
        return department.getInfo();
    }
}
