package com.example.dentalsaas.dto.response;

public class DashboardStatsDto {
    private long totalPatients;
    private long todayAppointments;
    private long thisWeekAppointments;
    private long thisMonthAppointments;
    private long pendingAppointments;
    private long completedAppointments;

    public DashboardStatsDto(long totalPatients, long todayAppointments,
                             long thisWeekAppointments, long thisMonthAppointments,
                             long pendingAppointments, long completedAppointments) {
        this.totalPatients = totalPatients;
        this.todayAppointments = todayAppointments;
        this.thisWeekAppointments = thisWeekAppointments;
        this.thisMonthAppointments = thisMonthAppointments;
        this.pendingAppointments = pendingAppointments;
        this.completedAppointments = completedAppointments;
    }

    // Getters et Setters
    public long getTotalPatients() { return totalPatients; }
    public void setTotalPatients(long totalPatients) { this.totalPatients = totalPatients; }
    public long getTodayAppointments() { return todayAppointments; }
    public void setTodayAppointments(long todayAppointments) { this.todayAppointments = todayAppointments; }
    public long getThisWeekAppointments() { return thisWeekAppointments; }
    public void setThisWeekAppointments(long thisWeekAppointments) { this.thisWeekAppointments = thisWeekAppointments; }
    public long getThisMonthAppointments() { return thisMonthAppointments; }
    public void setThisMonthAppointments(long thisMonthAppointments) { this.thisMonthAppointments = thisMonthAppointments; }
    public long getPendingAppointments() { return pendingAppointments; }
    public void setPendingAppointments(long pendingAppointments) { this.pendingAppointments = pendingAppointments; }
    public long getCompletedAppointments() { return completedAppointments; }
    public void setCompletedAppointments(long completedAppointments) { this.completedAppointments = completedAppointments; }
}