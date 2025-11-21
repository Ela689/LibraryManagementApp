package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.repository.UserRepository;
import com.example.librarymanagementapp.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private FileStorageService fileStorageService;

    // ✅ LOGIN PAGE
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ✅ REGISTER PAGE
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // ✅ REGISTER USER
    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam("idCardFile") MultipartFile file,
                           Model model) {
        try {
            String filePath = fileStorageService.saveFile(file);

            user.setIdCardFileName(file.getOriginalFilename());
            user.setIdCardFilePath(filePath);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("USER");
            user.setActive(false); // implicit inactive until admin approval
            user.setRegistrationDate(LocalDate.now());

            userRepository.save(user);

            model.addAttribute("success", "Account created successfully! Wait for admin approval.");
            return "login";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error uploading file. Please try again.");
            return "register";
        }
    }

    // ✅ LOGIN SUCCESS REDIRECT
    @PostMapping("/login-success")
    public String loginSuccess(@RequestParam String username, Model model) {
        model.addAttribute("username", username);
        User user = userRepository.findByUsername(username);
        if (user.getRole().equals("ADMIN")) {
            return "redirect:/home";
        } else {
            return "redirect:/user_wait";
        }
    }
}
