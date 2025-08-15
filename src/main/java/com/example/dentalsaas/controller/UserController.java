package com.example.dentalsaas.controller;

import com.example.dentalsaas.dto.request.CreateUserRequest;
import com.example.dentalsaas.dto.response.UserDto;
import com.example.dentalsaas.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userService.getClinicUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDto user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}