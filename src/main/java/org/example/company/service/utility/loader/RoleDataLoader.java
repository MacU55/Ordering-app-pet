package org.example.company.service.utility.loader;

import lombok.RequiredArgsConstructor;
import org.example.company.model.Role;
import org.example.company.repository.RoleRepository;
import org.example.company.security.model.RoleTypes;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleDataLoader implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        for(RoleTypes roleType : RoleTypes.values()) {
            if(roleRepository.findIdByRoleType(roleType).isEmpty()) {
                Role role = new Role();
                role.setRoleTypes(roleType);
                roleRepository.save(role);
            }
        }
    }
}
