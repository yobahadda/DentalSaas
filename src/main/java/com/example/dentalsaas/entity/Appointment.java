package com.example.dentalsaas.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateTime;
    private String notes;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "dentist_id")
    private User dentist;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;
}