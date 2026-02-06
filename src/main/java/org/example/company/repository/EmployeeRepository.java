package org.example.company.repository;

import java.util.List;
import java.util.Optional;
import org.example.company.models.Department;
import org.example.company.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByName(String name);
    Optional<Employee> findByEmail(String email);
    List<Employee> findByDepartment(Department department);
}
