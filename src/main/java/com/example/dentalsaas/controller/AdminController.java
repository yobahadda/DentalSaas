package com.example.dentalsaas.controller;

import com.example.dentalsaas.dto.response.AdminStatsDto;
import com.example.dentalsaas.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDto> getGlobalStats() {
        AdminStatsDto stats = adminService.getGlobalStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/clinics")
    public ResponseEntity<?> getAllClinics() {
        // Implementation for super admin to view all clinics
        return ResponseEntity.ok("Super admin functionality");
    }
}