package com.example.dentalsaas.service;

import com.example.dentalsaas.dto.request.CreateUserRequest;
import com.example.dentalsaas.dto.response.UserDto;
import com.example.dentalsaas.entity.User;
import com.example.dentalsaas.entity.Clinic;
import com.example.dentalsaas.repository.UserRepository;
import com.example.dentalsaas.repository.ClinicRepository;
import com.example.dentalsaas.exception.EntityNotFoundException;
import com.example.dentalsaas.exception.DuplicateEntityException;
import com.example.dentalsaas.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityUtils securityUtils;

    public List<UserDto> getClinicUsers() {
        Long clinicId = securityUtils.getCurrentUserClinicId();
        List<User> users = userRepository.findByClinicId(clinicId);
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(CreateUserRequest request) {
        Long clinicId = securityUtils.getCurrentUserClinicId();

        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEntityException("Cet email est déjà utilisé");
        }

        // Récupérer la clinique
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Clinique non trouvée"));

        // Créer l'utilisateur
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());
        user.setClinic(clinic);

        user = userRepository.save(user);
        return convertToDto(user);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        return dto;
    }
}