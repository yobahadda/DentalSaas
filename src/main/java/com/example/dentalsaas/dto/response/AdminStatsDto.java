package com.example.dentalsaas.dto.response;

public class AdminStatsDto {
    private long totalClinics;
    private long totalUsers;
    private long totalPatients;
    private long totalAppointments;
    private long basicPlanClinics;
    private long premiumPlanClinics;
    private long enterprisePlanClinics;

    // Constructor et getters/setters
    public AdminStatsDto(long totalClinics, long totalUsers, long totalPatients,
                         long totalAppointments, long basicPlanClinics,
                         long premiumPlanClinics, long enterprisePlanClinics) {
        this.totalClinics = totalClinics;
        this.totalUsers = totalUsers;
        this.totalPatients = totalPatients;
        this.totalAppointments = totalAppointments;
        this.basicPlanClinics = basicPlanClinics;
        this.premiumPlanClinics = premiumPlanClinics;
        this.enterprisePlanClinics = enterprisePlanClinics;
    }

    // Getters et Setters...
    public long getTotalClinics() { return totalClinics; }
    public void setTotalClinics(long totalClinics) { this.totalClinics = totalClinics; }
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getTotalPatients() { return totalPatients; }
    public void setTotalPatients(long totalPatients) { this.totalPatients = totalPatients; }
    public long getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(long totalAppointments) { this.totalAppointments = totalAppointments; }
    public long getBasicPlanClinics() { return basicPlanClinics; }
    public void setBasicPlanClinics(long basicPlanClinics) { this.basicPlanClinics = basicPlanClinics; }
    public long getPremiumPlanClinics() { return premiumPlanClinics; }
    public void setPremiumPlanClinics(long premiumPlanClinics) { this.premiumPlanClinics = premiumPlanClinics; }
    public long getEnterprisePlanClinics() { return enterprisePlanClinics; }
    public void setEnterprisePlanClinics(long enterprisePlanClinics) { this.enterprisePlanClinics = enterprisePlanClinics; }
}