package com.example.librarymanagementapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- date de bază ---
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    // --- rol & status ---
    @Column(nullable = false)
    private String role; // "ADMIN" sau "USER"

    @Column(nullable = false)
    private boolean active;

    // --- fișier carte de identitate ---
    private String idCardFileName;
    private String idCardFilePath;

    // --- data înregistrării ---
    private LocalDate registrationDate;

    // === CONSTRUCTORI ===
    public User() {
    }

    public User(String username, String password, String email, String phone, String role, boolean active) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.active = active;
        this.registrationDate = LocalDate.now();
    }

    // === GETTERS & SETTERS ===
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getIdCardFileName() {
        return idCardFileName;
    }

    public void setIdCardFileName(String idCardFileName) {
        this.idCardFileName = idCardFileName;
    }

    public String getIdCardFilePath() {
        return idCardFilePath;
    }

    public void setIdCardFilePath(String idCardFilePath) {
        this.idCardFilePath = idCardFilePath;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    // === UTILITAR ===
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", active=" + active +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
