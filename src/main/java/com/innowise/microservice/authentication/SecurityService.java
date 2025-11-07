package com.innowise.microservice.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    public boolean isOwnerOrAdmin(Long resourceOwnerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return true;

        try {
            Long currentUserId = Long.parseLong(auth.getName());
            return currentUserId.equals(resourceOwnerId);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}