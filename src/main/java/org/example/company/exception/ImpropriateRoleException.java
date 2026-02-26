package org.example.company.exception;

import java.util.Arrays;
import org.example.company.security.model.RoleTypes;

/**
 * Exception thrown when user's role doesn't have access to the requested operation.
 */
public class ImpropriateRoleException extends BaseException {

    private final RoleTypes currentRole;
    private final RoleTypes[] requiredRoles;

    public ImpropriateRoleException(RoleTypes currentRole, RoleTypes[] requiredRoles) {
        super(ImpropriateRoleException.class,
            buildMessage(currentRole, requiredRoles));
        this.currentRole = currentRole;
        this.requiredRoles = requiredRoles;
    }

    public ImpropriateRoleException(String message) {
        super(ImpropriateRoleException.class, message);
        this.currentRole = null;
        this.requiredRoles = null;
    }

    private static String buildMessage(RoleTypes currentRole, RoleTypes[] requiredRoles) {
        if (currentRole == null) {
            return String.format("No role provided. Required roles: %s", Arrays.toString(requiredRoles));
        }
        return String.format("Role %s is not allowed. Required roles: %s",
            currentRole, Arrays.toString(requiredRoles));
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.ACCESS_DENIED;
    }

    public RoleTypes getCurrentRole() {
        return currentRole;
    }

    public RoleTypes[] getRequiredRoles() {
        return requiredRoles;
    }
}
