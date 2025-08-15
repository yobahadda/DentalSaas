package com.example.dentalsaas.dto.response;

import com.example.dentalsaas.entity.AppointmentStatus;
import java.time.LocalDateTime;

public class AppointmentDto {
    private Long id;
    private LocalDateTime dateTime;
    private String notes;
    private AppointmentStatus status;
    private Long patientId;
    private String patientFirstName;
    private String patientLastName;
    private Long dentistId;
    private String dentistFirstName;
    private String dentistLastName;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public String getPatientFirstName() { return patientFirstName; }
    public void setPatientFirstName(String patientFirstName) { this.patientFirstName = patientFirstName; }
    public String getPatientLastName() { return patientLastName; }
    public void setPatientLastName(String patientLastName) { this.patientLastName = patientLastName; }
    public Long getDentistId() { return dentistId; }
    public void setDentistId(Long dentistId) { this.dentistId = dentistId; }
    public String getDentistFirstName() { return dentistFirstName; }
    public void setDentistFirstName(String dentistFirstName) { this.dentistFirstName = dentistFirstName; }
    public String getDentistLastName() { return dentistLastName; }
    public void setDentistLastName(String dentistLastName) { this.dentistLastName = dentistLastName; }
}
