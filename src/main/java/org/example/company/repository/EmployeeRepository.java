package org.example.company.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.company.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e.email FROM Employee e WHERE e.email LIKE :pattern")
    List<String> findEmailsByPattern(@Param("pattern") String pattern);

    @Query("SELECT e.id FROM Employee e WHERE e.email = :email")
    Optional<Long> findIdByEmail(@Param("email") String email);

    @Query(value = "SELECT role_id FROM employee_role WHERE employee_id = :employeeId", nativeQuery = true)
    Set<Long> findRolesIdByEmployeeId(@Param("employeeId") Long employeeId);

    Optional<Employee> findByUserName(String name);
    Optional<Employee> findByEmail(String email);

    @Modifying
    @Query(value = "INSERT INTO employee_role (role_id, employee_id) values (:role_id, :employee_id)", nativeQuery = true)
    void createRoleForEmployee(@Param("role_id") long roleId, @Param("employee_id") long employeeId);
//    List<Employee> findByDepartment(RoleTypes departmentRole);
}
