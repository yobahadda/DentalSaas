package com.example.dentalsaas.controller;

import com.example.dentalsaas.dto.request.CreateAppointmentRequest;
import com.example.dentalsaas.dto.request.UpdateAppointmentRequest;
import com.example.dentalsaas.dto.response.AppointmentDto;
import com.example.dentalsaas.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN') or hasRole('DENTIST') or hasRole('ASSISTANT')")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAppointments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/today")
    public ResponseEntity<List<AppointmentDto>> getTodayAppointments() {
        List<AppointmentDto> appointments = appointmentService.getTodayAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointment(@PathVariable Long id) {
        AppointmentDto appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTIST') or hasRole('ASSISTANT')")
    public ResponseEntity<AppointmentDto> scheduleAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        AppointmentDto appointment = appointmentService.scheduleAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTIST')")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentRequest request) {
        AppointmentDto appointment = appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok(appointment);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTIST') or hasRole('ASSISTANT')")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDto>> getPatientAppointments(@PathVariable Long patientId) {
        List<AppointmentDto> appointments = appointmentService.getPatientAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }
}