package com.example.dentalsaas.entity;
import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clinics")
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL)
    private List<User> users;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL)
    private List<Patient> patients;
}