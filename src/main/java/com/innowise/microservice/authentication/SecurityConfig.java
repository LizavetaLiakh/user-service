package com.innowise.microservice.authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationConverter jwtAuthenticationConverter)
            throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/users/internal/add").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/get/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/users/update/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/users/delete/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .build();
    }

    private AuthorizationManager<RequestAuthorizationContext> isOwnerOrAdmin() {
        return (authenticationSupplier, context) -> {
            Authentication authentication = authenticationSupplier.get();
            HttpServletRequest request = context.getRequest();

            String uri = request.getRequestURI();
            String pathUserId = uri.substring(uri.lastIndexOf("/") + 1);
            String currentUser = authentication.getName();

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isOwner = pathUserId.equals(currentUser);

            return new AuthorizationDecision(isAdmin || isOwner);
        };
    }
}