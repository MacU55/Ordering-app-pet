package org.example.company.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.company.dto.request.RequestEmployee;
import org.example.company.dto.response.ResponseEmployee;
import org.example.company.model.DepartmentRole;
import org.example.company.model.Employee;
import org.example.company.model.Role;
import org.example.company.repository.EmployeeRepository;
import org.example.company.service.utility.EmployeeEmailGenerator;
import org.example.company.service.utility.converter.EmployeeConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeConverter employeeConverter;

    @Transactional(readOnly = true)
    public Optional<ResponseEmployee> findEmployeeByName(String name) {
        return employeeRepository.findByName(name).map(employeeConverter::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Optional<ResponseEmployee> findEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email).map(employeeConverter::convertToDTO);
    }

    @Transactional(readOnly = true)
    public List<ResponseEmployee> findEmployeesByDepartment(DepartmentRole departmentRole) {
        return employeeRepository.findByDepartment(departmentRole).stream()
            .map(employeeConverter::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Employee with uuid " + id + " not found"));
    }

    @Transactional
    public ResponseEmployee saveEmployee(RequestEmployee requestEmployee) {
        Employee employee = employeeConverter.convertToEntity(requestEmployee);
        String baseName = EmployeeEmailGenerator.normalizeNameForEmail(employee.getName());
        String pattern = baseName + "%@ordering.com";
        var existingEmails = employeeRepository.findEmailsByPattern(pattern);
        String email = EmployeeEmailGenerator.generateNextAvailableEmail(baseName, existingEmails);
        employee.setEmail(email);
        employee = employeeRepository.save(employee);
        return employeeConverter.convertToDTO(employee);
    }

    @Transactional
    public ResponseEmployee updateEmployee(long employeeId, RequestEmployee requestEmployee) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee with uuid " + employeeId + " not found"));
        employee.updateEmployee(requestEmployee);
        return employeeConverter.convertToDTO(employee);
    }

    @Transactional
    public void deleteEmployee(long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        } else {
            log.error("Employee with uuid {} not found", id);
            throw new EntityNotFoundException("Employee with uuid " + id + " not found");
        }
    }

    @Transactional(readOnly = true)
    public Set<DepartmentRole.DepartmentInfo> getDepartmentInfoByEmployeeId(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new EntityNotFoundException("Employee with uuid " + employeeId + " not found"));
        Set<DepartmentRole> departmentRoles =
            employee.getRoles().stream().map(Role::getDepartmentRole).collect(Collectors.toSet());
        return departmentRoles.stream().map(DepartmentRole::getInfo).collect(Collectors.toSet());
    }
}
