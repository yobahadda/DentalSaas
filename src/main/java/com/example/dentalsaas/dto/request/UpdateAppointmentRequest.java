package com.example.dentalsaas.dto.request;

import com.example.dentalsaas.entity.AppointmentStatus;
import java.time.LocalDateTime;

public class UpdateAppointmentRequest {
    private LocalDateTime dateTime;
    private String notes;
    private AppointmentStatus status;
    private Long dentistId;

    // Getters et Setters
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public Long getDentistId() { return dentistId; }
    public void setDentistId(Long dentistId) { this.dentistId = dentistId; }
}