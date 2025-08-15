package com.example.dentalsaas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class UpdatePatientRequest {
    private String firstName;
    private String lastName;

    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate birthDate;

    @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "Format de téléphone invalide")
    private String phone;

    @Email(message = "Format email invalide")
    private String email;

    private String address;

    // Getters et Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}