package com.example.librarymanagementapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String phone;

    private String role; // USER sau ADMIN
    private boolean active; // validat de admin
    private String idCardFile; // numele fișierului uploadat (pdf/img)
    private LocalDate registrationDate;

    public User() {
        this.registrationDate = LocalDate.now();
        this.active = false;
        this.role = "USER";
    }

    // Getteri și setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getIdCardFile() { return idCardFile; }
    public void setIdCardFile(String idCardFile) { this.idCardFile = idCardFile; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
}
