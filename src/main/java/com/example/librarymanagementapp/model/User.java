//User.java
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
    private String email;
    private String password;
    private String phone;
    private String role;

    private boolean active;
    private boolean enabled = true; // ✅ nou: activare cont

    private String idCardFileName;
    private String idCardFilePath;

    private LocalDate registrationDate;

    // Getteri și setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isEnabled() { return enabled; }        // ✅ getter nou
    public void setEnabled(boolean enabled) { this.enabled = enabled; } // ✅ setter nou

    public String getIdCardFileName() { return idCardFileName; }
    public void setIdCardFileName(String idCardFileName) { this.idCardFileName = idCardFileName; }

    public String getIdCardFilePath() { return idCardFilePath; }
    public void setIdCardFilePath(String idCardFilePath) { this.idCardFilePath = idCardFilePath; }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
}
