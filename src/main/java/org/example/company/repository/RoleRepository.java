package org.example.company.repository;

import java.util.Optional;
import java.util.Set;
import org.example.company.model.Role;
import org.example.company.security.model.RoleTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Set<Role> findRolesByIdIn(Set<Long> rolesId);

    @Query(value = "SELECT r.id FROM Role r WHERE r.roleTypes = :roleType ")
    Optional<Long> findIdByRoleType(@Param("roleType") RoleTypes roleType);

}
