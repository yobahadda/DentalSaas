package com.example.dentalsaas.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateAppointmentRequest {
    @NotNull(message = "La date et l'heure sont obligatoires")
    @Future(message = "Le rendez-vous doit être dans le futur")
    private LocalDateTime dateTime;

    private String notes;

    @NotNull(message = "Le patient est obligatoire")
    private Long patientId;

    @NotNull(message = "Le dentiste est obligatoire")
    private Long dentistId;

    // Getters et Setters
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Long getDentistId() { return dentistId; }
    public void setDentistId(Long dentistId) { this.dentistId = dentistId; }
}
