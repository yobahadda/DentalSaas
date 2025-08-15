package com.example.dentalsaas.service;

import com.example.dentalsaas.dto.request.CreateAppointmentRequest;
import com.example.dentalsaas.dto.request.UpdateAppointmentRequest;
import com.example.dentalsaas.dto.response.AppointmentDto;
import com.example.dentalsaas.entity.Appointment;
import com.example.dentalsaas.entity.Patient;
import com.example.dentalsaas.entity.User;
import com.example.dentalsaas.entity.AppointmentStatus;
import com.example.dentalsaas.repository.AppointmentRepository;
import com.example.dentalsaas.repository.PatientRepository;
import com.example.dentalsaas.repository.UserRepository;
import com.example.dentalsaas.exception.EntityNotFoundException;
import com.example.dentalsaas.exception.AppointmentConflictException;
import com.example.dentalsaas.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityUtils securityUtils;

    public List<AppointmentDto> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        Long clinicId = securityUtils.getCurrentUserClinicId();
        List<Appointment> appointments = appointmentRepository.findByClinicIdAndDateBetween(
                clinicId, startDate, endDate);
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getTodayAppointments() {
        Long clinicId = securityUtils.getCurrentUserClinicId();
        List<Appointment> appointments = appointmentRepository.findTodayAppointmentsByClinic(clinicId);
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto getAppointmentById(Long appointmentId) {
        Long clinicId = securityUtils.getCurrentUserClinicId();
        Appointment appointment = appointmentRepository.findByIdAndClinicId(appointmentId, clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé"));
        return convertToDto(appointment);
    }

    public AppointmentDto scheduleAppointment(CreateAppointmentRequest request) {
        Long clinicId = securityUtils.getCurrentUserClinicId();

        // Vérifier que le patient appartient à la clinique
        Patient patient = patientRepository.findByIdAndClinicId(request.getPatientId(), clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé"));

        // Vérifier que le dentiste appartient à la clinique
        User dentist = userRepository.findByIdAndClinicId(request.getDentistId(), clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Dentiste non trouvé"));

        // Vérifier les conflits d'horaire
        if (hasConflict(request.getDentistId(), request.getDateTime(), 30)) {
            throw new AppointmentConflictException("Créneau déjà occupé pour ce dentiste");
        }

        // Créer le rendez-vous
        Appointment appointment = new Appointment();
        appointment.setDateTime(request.getDateTime());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setPatient(patient);
        appointment.setDentist(dentist);
        appointment.setClinic(patient.getClinic());

        appointment = appointmentRepository.save(appointment);
        return convertToDto(appointment);
    }

    public AppointmentDto updateAppointment(Long appointmentId, UpdateAppointmentRequest request) {
        Long clinicId = securityUtils.getCurrentUserClinicId();

        Appointment appointment = appointmentRepository.findByIdAndClinicId(appointmentId, clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé"));

        // Vérifier les conflits si la date/heure ou le dentiste change
        if (request.getDateTime() != null || request.getDentistId() != null) {
            LocalDateTime newDateTime = request.getDateTime() != null ?
                    request.getDateTime() : appointment.getDateTime();
            Long newDentistId = request.getDentistId() != null ?
                    request.getDentistId() : appointment.getDentist().getId();

            if (hasConflictExcluding(newDentistId, newDateTime, 30, appointmentId)) {
                throw new AppointmentConflictException("Créneau déjà occupé pour ce dentiste");
            }
        }

        // Mettre à jour les champs
        if (request.getDateTime() != null) {
            appointment.setDateTime(request.getDateTime());
        }
        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }
        if (request.getStatus() != null) {
            appointment.setStatus(request.getStatus());
        }
        if (request.getDentistId() != null) {
            User dentist = userRepository.findByIdAndClinicId(request.getDentistId(), clinicId)
                    .orElseThrow(() -> new EntityNotFoundException("Dentiste non trouvé"));
            appointment.setDentist(dentist);
        }

        appointment = appointmentRepository.save(appointment);
        return convertToDto(appointment);
    }

    public void cancelAppointment(Long appointmentId) {
        Long clinicId = securityUtils.getCurrentUserClinicId();

        Appointment appointment = appointmentRepository.findByIdAndClinicId(appointmentId, clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Rendez-vous non trouvé"));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    public List<AppointmentDto> getPatientAppointments(Long patientId) {
        Long clinicId = securityUtils.getCurrentUserClinicId();

        // Vérifier que le patient appartient à la clinique
        patientRepository.findByIdAndClinicId(patientId, clinicId)
                .orElseThrow(() -> new EntityNotFoundException("Patient non trouvé"));

        List<Appointment> appointments = appointmentRepository
                .findByPatientIdAndClinicIdOrderByDateTimeDesc(patientId, clinicId);

        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private boolean hasConflict(Long dentistId, LocalDateTime dateTime, int durationMinutes) {
        return hasConflictExcluding(dentistId, dateTime, durationMinutes, null);
    }

    private boolean hasConflictExcluding(Long dentistId, LocalDateTime dateTime,
                                         int durationMinutes, Long excludeAppointmentId) {
        LocalDateTime endTime = dateTime.plusMinutes(durationMinutes);

        List<Appointment> existingAppointments = appointmentRepository
                .findByDentistIdAndClinicIdAndDateTimeBetween(
                        dentistId,
                        securityUtils.getCurrentUserClinicId(),
                        dateTime.toLocalDate().atStartOfDay(),
                        dateTime.toLocalDate().atTime(23, 59, 59)
                );

        return existingAppointments.stream()
                .filter(apt -> excludeAppointmentId == null || !apt.getId().equals(excludeAppointmentId))
                .filter(apt -> apt.getStatus() != AppointmentStatus.CANCELLED)
                .anyMatch(apt -> {
                    LocalDateTime existingStart = apt.getDateTime();
                    LocalDateTime existingEnd = existingStart.plusMinutes(30); // Durée standard

                    // Vérifier le chevauchement
                    return dateTime.isBefore(existingEnd) && endTime.isAfter(existingStart);
                });
    }

    private AppointmentDto convertToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setDateTime(appointment.getDateTime());
        dto.setNotes(appointment.getNotes());
        dto.setStatus(appointment.getStatus());

        // Patient info
        dto.setPatientId(appointment.getPatient().getId());
        dto.setPatientFirstName(appointment.getPatient().getFirstName());
        dto.setPatientLastName(appointment.getPatient().getLastName());

        // Dentist info
        dto.setDentistId(appointment.getDentist().getId());
        dto.setDentistFirstName(appointment.getDentist().getFirstName());
        dto.setDentistLastName(appointment.getDentist().getLastName());

        return dto;
    }
}
