package org.example.company.repository;

import java.util.List;
import java.util.Optional;
import org.example.company.model.DepartmentRole;
import org.example.company.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e.email FROM Employee e WHERE e.email LIKE :pattern")
    List<String> findEmailsByPattern(@Param("pattern") String pattern);

    Optional<Employee> findByName(String name);
    Optional<Employee> findByEmail(String email);
    List<Employee> findByDepartment(DepartmentRole departmentRole);
}
