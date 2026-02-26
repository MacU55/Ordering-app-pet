package org.example.company.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.company.model.Role;
import org.example.company.repository.CustomerRepository;
import org.example.company.repository.EmployeeRepository;
import org.example.company.repository.RoleRepository;
import org.example.company.security.model.RoleTypes;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleFilter extends OncePerRequestFilter {

    public static final String USER_EMAIL_HEADER = "X-User-Email";
    public static final String USER_ROLE_HEADER = "X-User-Role";

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String emailHeader = request.getHeader(USER_EMAIL_HEADER);
            String roleHeader = request.getHeader(USER_ROLE_HEADER);

            if (roleHeader != null && roleHeader.equals("SUPER_ADMIN") &&
                (emailHeader != null && !emailHeader.isBlank())) {
                this.setAuthenticationForSuperAdmin(emailHeader);
            }

            else if  (emailHeader != null && !emailHeader.isBlank()) {
                emailHeader = emailHeader.trim().toLowerCase();
                Optional<Long> employeeId = employeeRepository.findIdByEmail(emailHeader);
                if (employeeId.isPresent()) {
                    this.setAuthenticationForEmployee(employeeId.get(), emailHeader);
                } else {
                    Optional<Long> customerId = customerRepository.findIdByEmail(emailHeader);
                    if (customerId.isPresent()) {
                        this.setAuthenticationForCustomer(emailHeader);
                    }
                }
            } else {
                log.debug("No {} header provided", USER_EMAIL_HEADER);
            }
            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid user in header: {}", e.getMessage());
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private void setAuthenticationForEmployee(Long employeeId, String employeeEmail) {
        Set<Long> rolesIdByEmployeeId = employeeRepository.findRolesIdByEmployeeId(employeeId);
        Set<RoleTypes> departmentRoleTypesSet = roleRepository.findRolesByIdIn(rolesIdByEmployeeId)
            .stream()
            .map(Role::getRoleTypes)
            .collect(Collectors.toSet());
        List<GrantedAuthority> grantedAuthorities =
            departmentRoleTypesSet.stream().map(role -> new SimpleGrantedAuthority(role.name())
            ).collect(Collectors.toList());

        var token = new UsernamePasswordAuthenticationToken(employeeEmail, null, grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(token);
        log.debug("Employee roles set in SecurityContext. employeeEmail: {}", employeeEmail);
    }

    private void setAuthenticationForCustomer(String customerEmail) {
        var simpleGrantedAuthority = new SimpleGrantedAuthority(RoleTypes.CUSTOMER.name());
        var token = new UsernamePasswordAuthenticationToken(customerEmail, null, List.of(simpleGrantedAuthority));
        SecurityContextHolder.getContext().setAuthentication(token);
        log.debug("Customer role set in SecurityContext. customerEmail: {}", customerEmail);
    }

    private void setAuthenticationForSuperAdmin(String adminEmail) {
        var simpleGrantedAuthority = new SimpleGrantedAuthority(RoleTypes.SUPER_ADMIN.name());
        var token = new UsernamePasswordAuthenticationToken(adminEmail, null, List.of(simpleGrantedAuthority));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

}
