package com.example.dentalsaas.controller;

import com.example.dentalsaas.dto.request.LoginRequest;
import com.example.dentalsaas.dto.request.ClinicRegistrationRequest;
import com.example.dentalsaas.dto.response.JwtResponse;
import com.example.dentalsaas.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-clinic")
    public ResponseEntity<String> registerClinic(@Valid @RequestBody ClinicRegistrationRequest request) {
        authService.registerClinic(request);
        return ResponseEntity.ok("Cabinet dentaire créé avec succès");
    }
}