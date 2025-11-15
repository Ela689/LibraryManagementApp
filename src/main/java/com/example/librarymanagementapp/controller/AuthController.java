package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ important: folosește PasswordEncoder, nu BCrypt direct

    private final String uploadDir = System.getProperty("user.dir") + "/uploads/idcards/";

    // === LOGIN PAGE ===
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid credentials or account not active!");
        }
        return "login";
    }

    // === REGISTER PAGE ===
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam("idCardFile") MultipartFile file,
                           Model model) {
        try {
            // 1️⃣ Validare username unic
            if (userRepository.findByUsername(user.getUsername()) != null) {
                model.addAttribute("error", "Username already exists!");
                return "register";
            }

            // 2️⃣ Validare telefon (simplu, format RO: începe cu 07 și are 10 cifre)
            if (!user.getPhone().matches("^07\\d{8}$")) {
                model.addAttribute("error", "Invalid Romanian phone number format!");
                return "register";
            }

            // 3️⃣ Verificare fișier încărcat
            if (file == null || file.isEmpty()) {
                model.addAttribute("error", "Please upload a copy of your ID card!");
                return "register";
            }

            // 4️⃣ Creăm folderul dacă nu există
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            // 5️⃣ Salvăm fișierul în folderul uploads/idcards
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path path = Paths.get(uploadDir + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 6️⃣ Completăm datele utilizatorului
            user.setIdCardFileName(fileName);
            user.setIdCardFilePath(path.toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("USER");
            user.setActive(false); // inactiv până este aprobat de admin
            user.setRegistrationDate(LocalDate.now());

            userRepository.save(user);

            model.addAttribute("success", "Account created successfully! Await admin approval.");
            return "login";

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error uploading file: " + e.getMessage());
            return "register";
        }
    }

    // === HOME PAGE ===
    //@GetMapping("/home")
    //public String home() {
      //  return "home";
    //}
}
