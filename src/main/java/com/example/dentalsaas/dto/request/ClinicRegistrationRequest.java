package com.example.dentalsaas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClinicRegistrationRequest {
    @NotBlank(message = "Le nom du cabinet est obligatoire")
    private String clinicName;

    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String phone;

    @NotBlank(message = "L'email du cabinet est obligatoire")
    @Email(message = "Format email invalide")
    private String clinicEmail;

    @NotBlank(message = "Le prénom de l'administrateur est obligatoire")
    private String adminFirstName;

    @NotBlank(message = "Le nom de l'administrateur est obligatoire")
    private String adminLastName;

    @NotBlank(message = "L'email de l'administrateur est obligatoire")
    @Email(message = "Format email invalide")
    private String adminEmail;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    // Getters et Setters
    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getClinicEmail() { return clinicEmail; }
    public void setClinicEmail(String clinicEmail) { this.clinicEmail = clinicEmail; }
    public String getAdminFirstName() { return adminFirstName; }
    public void setAdminFirstName(String adminFirstName) { this.adminFirstName = adminFirstName; }
    public String getAdminLastName() { return adminLastName; }
    public void setAdminLastName(String adminLastName) { this.adminLastName = adminLastName; }
    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
