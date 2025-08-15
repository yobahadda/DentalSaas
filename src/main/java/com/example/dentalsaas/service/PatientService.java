package com.example.dentalsaas.service;

//package com.example.dentalsaas.service;

import com.example.dentalsaas.dto.request.CreatePatientRequest;
import com.example.dentalsaas.dto.request.UpdatePatientRequest;
import com.example.dentalsaas.dto.response.PatientDto;
import com.example.dentalsaas.entity.Patient;
import com.example.dentalsaas.entity.Clinic;
import com.example.dentalsaas.repository.PatientRepository;
import com.example.dentalsaas.repository.ClinicRepository;
import com.example.dentalsaas.exception.EntityNotFoundException;
import com.example.dentalsaas.exception.DuplicateEntityException;
import com.example.dentalsaas.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private SecurityUtils securityUtils;

    public List<PatientDto> getPatientsByClinic() {
        Long clinicId = securityUtils.getCurrentUserClinicId();
        List<Patient> patients = patientRepository.findByClinicId(clinicId);
        return patients.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<PatientDto> getPatientsByClinic(Pageable pageable) {
        Long clinicId = securityUtils.getCurrentUserClinicId();
        Page<Patient> patients = patientRepository.findByClinicId(clinicId, pageable);
        return patients.map(this::convertToDto);
    }

    public PatientDto getPatientById(Long patientId) {
        Long clinicId = securityUtils.getCurrentUserClinicId();
        Patient patient = patientRepository.findByIdAndClinicId(patientId, clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé"));
        return convertToDto(patient);
    }

    public PatientDto createPatient(CreatePatientRequest request) {
        Long clinicId = securityUtils.getCurrentUserClinicId();

        // Vérifier les doublons
        if (request.getEmail() != null &&
                patientRepository.existsByEmailAndClinicId(request.getEmail(), clinicId)) {
            throw new DuplicateEntityException("Un patient avec cet email existe déjà");
        }

        if (request.getPhone() != null &&
                patientRepository.existsByPhoneAndClinicId(request.getPhone(), clinicId)) {
            throw new DuplicateEntityException("Un patient avec ce téléphone existe déjà");
        }

        // Récupérer la clinique
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Clinique non trouvée"));

        // Créer le patient
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setBirthDate(request.getBirthDate());
        patient.setPhone(request.getPhone());
        patient.setEmail(request.getEmail());
        patient.setAddress(request.getAddress());
        patient.setClinic(clinic);

        patient = patientRepository.save(patient);
        return convertToDto(patient);
    }

    public PatientDto updatePatient(Long patientId, UpdatePatientRequest request) {
        Long clinicId = securityUtils.getCurrentUserClinicId();

        Patient patient = patientRepository.findByIdAndClinicId(patientId, clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé"));

        // Vérifier les doublons (exclure le patient actuel)
        if (request.getEmail() != null && !request.getEmail().equals(patient.getEmail()) &&
                patientRepository.existsByEmailAndClinicId(request.getEmail(), clinicId)) {
            throw new DuplicateEntityException("Un patient avec cet email existe déjà");
        }

        // Mettre à jour les champs
        if (request.getFirstName() != null) {
            patient.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            patient.setLastName(request.getLastName());
        }
        if (request.getBirthDate() != null) {
            patient.setBirthDate(request.getBirthDate());
        }
        if (request.getPhone() != null) {
            patient.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            patient.setEmail(request.getEmail());
        }
        if (request.getAddress() != null) {
            patient.setAddress(request.getAddress());
        }

        patient = patientRepository.save(patient);
        return convertToDto(patient);
    }

    public void deletePatient(Long patientId) {
        Long clinicId = securityUtils.getCurrentUserClinicId();

        Patient patient = patientRepository.findByIdAndClinicId(patientId, clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé"));

        patientRepository.delete(patient);
    }

    public List<PatientDto> searchPatients(String searchTerm) {
        Long clinicId = securityUtils.getCurrentUserClinicId();
        List<Patient> patients = patientRepository.searchPatients(clinicId, searchTerm);
        return patients.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PatientDto> getPatientsBirthdayToday() {
        Long clinicId = securityUtils.getCurrentUserClinicId();
        List<Patient> patients = patientRepository.findPatientsBirthdayToday(clinicId, LocalDate.now());
        return patients.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PatientDto convertToDto(Patient patient) {
        PatientDto dto = new PatientDto();
        dto.setId(patient.getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setBirthDate(patient.getBirthDate());
        dto.setPhone(patient.getPhone());
        dto.setEmail(patient.getEmail());
        dto.setAddress(patient.getAddress());

        // Calculer l'âge
        if (patient.getBirthDate() != null) {
            dto.setAge(Period.between(patient.getBirthDate(), LocalDate.now()).getYears());
        }

        return dto;
    }
}