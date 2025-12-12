package com.innowise.microservice.authentication;

import com.innowise.microservice.repository.CardInfoRepository;
import com.innowise.microservice.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    private final UserRepository userRepository;
    private final CardInfoRepository cardInfoRepository;

    public SecurityService(UserRepository userRepository, CardInfoRepository cardInfoRepository) {
        this.userRepository = userRepository;
        this.cardInfoRepository = cardInfoRepository;
    }

    public boolean isOwnerOrAdmin(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        if (hasAdminRole(auth)) {
            return true;
        }

        String currentEmail = auth.getName();
        return userRepository.findById(userId)
                .map(user -> user.getEmail().equals(currentEmail))
                .orElse(false);
    }

    public boolean isCardOwnerOrAdmin(Long cardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        if (hasAdminRole(authentication)) {
            return true;
        }

        String currentEmail = authentication.getName();
        return cardInfoRepository.findById(cardId)
                .map(card -> card.getUser().getEmail().equals(currentEmail))
                .orElse(false);
    }

    public boolean isOwnerOrAdminByEmail(String email) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return true;

        String currentEmail = auth.getName();
        return currentEmail.equals(email);
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}