package com.example.dentalsaas.security;

import com.example.dentalsaas.entity.User;
import com.example.dentalsaas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    @Autowired
    private UserRepository userRepository;

    public Long getCurrentUserClinicId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            return user.getClinic().getId();
        }
        throw new RuntimeException("Utilisateur non authentifié");
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        }
        throw new RuntimeException("Utilisateur non authentifié");
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        throw new RuntimeException("Utilisateur non authentifié");
    }
}