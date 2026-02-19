package org.example.company.security.aspect;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.company.exception.ImpropriateRoleException;
import org.example.company.security.annotation.IsAllowedByRole;
import org.example.company.security.context.RoleContext;
import org.example.company.security.model.Roles;
import org.springframework.stereotype.Component;

/**
 * Aspect that intercepts methods annotated with @IsAllowedByRole
 * and checks if the current user's role has access.
 */
@Slf4j
@Aspect
@Component
public class RoleAspect {

    @Before("@annotation(isAllowedByRole)")
    public void checkRole(IsAllowedByRole isAllowedByRole) {
        Roles currentRole = RoleContext.getRole();
        Roles[] allowedRoles = isAllowedByRole.value();

        log.debug("Checking access. Current role: {}, Allowed roles: {}",
            currentRole, Arrays.toString(allowedRoles));

        if (currentRole == null) {
            log.warn("Access denied: no role provided");
            throw new ImpropriateRoleException(null, allowedRoles);
        }

        boolean hasAccess = Arrays.asList(allowedRoles).contains(currentRole);

        if (!hasAccess) {
            log.warn("Access denied for role: {}. Required: {}", currentRole, Arrays.toString(allowedRoles));
            throw new ImpropriateRoleException(currentRole, allowedRoles);
        }

        log.debug("Access granted for role: {}", currentRole);
    }
}
