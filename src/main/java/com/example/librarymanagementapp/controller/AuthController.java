package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final String uploadDir = "uploads/idcards";

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
            // 1️⃣ Salvăm fișierul în folderul uploads/idcards
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 2️⃣ Completăm datele utilizatorului
            user.setIdCardFileName(fileName);
            user.setIdCardFilePath(filePath.toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("USER");
            user.setActive(false); // ❗ implicit inactiv până la aprobare
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


    // ✅ PAGE FOR NORMAL USER
   // @GetMapping("/user_wait")
    //public String showUserWaitPage(Model model, @SessionAttribute(name = "username", required = false) String username) {
      //  if (username != null) {
        //    User user = userRepository.findByUsername(username);
           // model.addAttribute("user", user);
        //}
        //return "user_wait";
    //}

    // ✅ SALVARE USER ÎN SESIUNE DUPĂ LOGIN
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
