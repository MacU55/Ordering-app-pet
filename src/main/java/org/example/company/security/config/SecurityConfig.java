package org.example.company.security.config;

import lombok.RequiredArgsConstructor;
import org.example.company.security.filter.RoleFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Minimal Spring Security configuration.
 * Authentication is handled via X-User-Role header (for development/testing).
 * Authorization is handled via @IsAllowedByRole annotation and RoleAspect.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class SecurityConfig {

    private final RoleFilter roleFilter;

    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .addFilterBefore(roleFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
