package com.example.dentalsaas.dto.response;

import com.example.dentalsaas.entity.Role;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private Long clinicId;
    private String clinicName;

    public JwtResponse(String token, Long userId, String email, String firstName,
                       String lastName, Role role, Long clinicId, String clinicName) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.clinicId = clinicId;
        this.clinicName = clinicName;
    }

    // Getters et Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Long getClinicId() { return clinicId; }
    public void setClinicId(Long clinicId) { this.clinicId = clinicId; }
    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }
}
