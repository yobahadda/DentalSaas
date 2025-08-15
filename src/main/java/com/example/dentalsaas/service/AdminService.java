package com.example.dentalsaas.service;

import com.example.dentalsaas.dto.response.AdminStatsDto;
import com.example.dentalsaas.entity.SubscriptionPlan;
import com.example.dentalsaas.repository.ClinicRepository;
import com.example.dentalsaas.repository.UserRepository;
import com.example.dentalsaas.repository.PatientRepository;
import com.example.dentalsaas.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private ClinicRepository clinicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public AdminStatsDto getGlobalStats() {
        long totalClinics = clinicRepository.count();
        long totalUsers = userRepository.count();
        long totalPatients = patientRepository.count();
        long totalAppointments = appointmentRepository.count();

        long basicPlanClinics = clinicRepository.countBySubscriptionPlan(SubscriptionPlan.BASIC);
        long premiumPlanClinics = clinicRepository.countBySubscriptionPlan(SubscriptionPlan.PREMIUM);
        long enterprisePlanClinics = clinicRepository.countBySubscriptionPlan(SubscriptionPlan.ENTERPRISE);

        return new AdminStatsDto(
                totalClinics,
                totalUsers,
                totalPatients,
                totalAppointments,
                basicPlanClinics,
                premiumPlanClinics,
                enterprisePlanClinics
        );
    }
}