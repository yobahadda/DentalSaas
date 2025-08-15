package com.example.dentalsaas.controller;

import com.example.dentalsaas.dto.response.DashboardStatsDto;
import com.example.dentalsaas.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN') or hasRole('DENTIST')")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
}