package com.example.dentalsaas.controller;

import com.example.dentalsaas.dto.request.CreatePatientRequest;
import com.example.dentalsaas.dto.request.UpdatePatientRequest;
import com.example.dentalsaas.dto.response.PatientDto;
import com.example.dentalsaas.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN') or hasRole('DENTIST') or hasRole('ASSISTANT')")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientDto>> getPatients() {
        List<PatientDto> patients = patientService.getPatientsByClinic();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<PatientDto>> getPatientsPaged(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<PatientDto> patients = patientService.getPatientsByClinic(pageable);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable Long id) {
        PatientDto patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTIST')")
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody CreatePatientRequest request) {
        PatientDto patient = patientService.createPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DENTIST')")
    public ResponseEntity<PatientDto> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatientRequest request) {
        PatientDto patient = patientService.updatePatient(id, request);
        return ResponseEntity.ok(patient);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<PatientDto>> searchPatients(@RequestParam String q) {
        List<PatientDto> patients = patientService.searchPatients(q);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/birthdays-today")
    public ResponseEntity<List<PatientDto>> getBirthdaysToday() {
        List<PatientDto> patients = patientService.getPatientsBirthdayToday();
        return ResponseEntity.ok(patients);
    }
}
