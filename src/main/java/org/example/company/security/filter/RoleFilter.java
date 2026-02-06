package org.example.company.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.example.company.security.context.RoleContext;
import org.example.company.security.model.Roles;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class RoleFilter extends OncePerRequestFilter {

    public static final String ROLE_HEADER = "X-User-Role";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String roleHeader = request.getHeader(ROLE_HEADER);

            if (roleHeader != null && !roleHeader.isBlank()) {
                try {
                    Roles role = Roles.valueOf(roleHeader.toUpperCase().trim());
                    RoleContext.setRole(role);
                    log.debug("Role set from header: {}", role);
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid role in header: {}", roleHeader);
                }
            } else {
                log.debug("No {} header provided", ROLE_HEADER);
            }

            filterChain.doFilter(request, response);

        } finally {
            RoleContext.clear();
        }
    }
}
