package com.example.dentalsaas.service;

import com.example.dentalsaas.dto.request.LoginRequest;
import com.example.dentalsaas.dto.request.ClinicRegistrationRequest;
import com.example.dentalsaas.dto.response.JwtResponse;
import com.example.dentalsaas.entity.Clinic;
import com.example.dentalsaas.entity.User;
import com.example.dentalsaas.entity.Role;
import com.example.dentalsaas.entity.SubscriptionPlan;
import com.example.dentalsaas.repository.ClinicRepository;
import com.example.dentalsaas.repository.UserRepository;
import com.example.dentalsaas.security.JwtUtil;
import com.example.dentalsaas.exception.EntityNotFoundException;
import com.example.dentalsaas.exception.DuplicateEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtResponse login(LoginRequest request) {
        // Authentification
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Récupérer l'utilisateur
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        // Générer le token JWT
        String token = jwtUtil.generateToken(user);

        return new JwtResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getClinic().getId(),
                user.getClinic().getName()
        );
    }

    public void registerClinic(ClinicRegistrationRequest request) {
        // Vérifier si l'email existe déjà
        if (clinicRepository.existsByEmail(request.getClinicEmail())) {
            throw new DuplicateEntityException("Cette adresse email est déjà utilisée");
        }

        if (userRepository.existsByEmail(request.getAdminEmail())) {
            throw new DuplicateEntityException("Cet email administrateur est déjà utilisé");
        }

        // Créer la clinique
        Clinic clinic = new Clinic();
        clinic.setName(request.getClinicName());
        clinic.setAddress(request.getAddress());
        clinic.setPhone(request.getPhone());
        clinic.setEmail(request.getClinicEmail());
        clinic.setSubscriptionPlan(SubscriptionPlan.BASIC); // Plan par défaut
        clinic.setCreatedAt(LocalDateTime.now());

        clinic = clinicRepository.save(clinic);

        // Créer l'utilisateur administrateur
        User admin = new User();
        admin.setEmail(request.getAdminEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setFirstName(request.getAdminFirstName());
        admin.setLastName(request.getAdminLastName());
        admin.setRole(Role.ADMIN);
        admin.setClinic(clinic);

        userRepository.save(admin);
    }
}