package org.example.company.security.context;

import org.example.company.security.model.Roles;

public final class RoleContext {

    private static final ThreadLocal<Roles> currentRole = new ThreadLocal<>();

    private RoleContext() {
        // Utility class, prevent instantiation
    }

    public static void setRole(Roles role) {
        currentRole.set(role);
    }

    public static Roles getRole() {
        return currentRole.get();
    }

    public static void clear() {
        currentRole.remove();
    }

    public static boolean hasRole() {
        return currentRole.get() != null;
    }
}
