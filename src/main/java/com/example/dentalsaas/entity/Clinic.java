package com.example.dentalsaas.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clinics")
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_plan", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Patient> patients;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    // Constructeurs
    public Clinic() {}

    public Clinic(String name, String address, String phone, String email, SubscriptionPlan subscriptionPlan) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.subscriptionPlan = subscriptionPlan;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Méthodes lifecycle
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public SubscriptionPlan getSubscriptionPlan() { return subscriptionPlan; }
    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<User> getUsers() { return users; }
    public void setUsers(List<User> users) { this.users = users; }

    public List<Patient> getPatients() { return patients; }
    public void setPatients(List<Patient> patients) { this.patients = patients; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
}