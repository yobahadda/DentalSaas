package com.example.dentalsaas.service;

import com.example.dentalsaas.dto.response.DashboardStatsDto;
import com.example.dentalsaas.entity.AppointmentStatus;
import com.example.dentalsaas.repository.PatientRepository;
import com.example.dentalsaas.repository.AppointmentRepository;
import com.example.dentalsaas.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
public class DashboardService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private SecurityUtils securityUtils;

    public DashboardStatsDto getDashboardStats() {
        Long clinicId = securityUtils.getCurrentUserClinicId();

        // Total patients
        long totalPatients = patientRepository.countPatientsByClinic(clinicId);

        // Rendez-vous aujourd'hui
        long todayAppointments = appointmentRepository.countTodayAppointmentsByClinic(clinicId);

        // Rendez-vous cette semaine
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        long thisWeekAppointments = appointmentRepository.findByClinicIdAndDateBetween(
                clinicId, startOfWeek, endOfWeek).size();

        // Rendez-vous ce mois
        LocalDate startOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        long thisMonthAppointments = appointmentRepository.findByClinicIdAndDateBetween(
                clinicId, startOfMonth, endOfMonth).size();

        // Rendez-vous en attente
        long pendingAppointments = appointmentRepository.countAppointmentsByClinicAndStatus(
                clinicId, AppointmentStatus.SCHEDULED);

        // Rendez-vous terminés
        long completedAppointments = appointmentRepository.countAppointmentsByClinicAndStatus(
                clinicId, AppointmentStatus.COMPLETED);

        return new DashboardStatsDto(
                totalPatients,
                todayAppointments,
                thisWeekAppointments,
                thisMonthAppointments,
                pendingAppointments,
                completedAppointments
        );
    }
}