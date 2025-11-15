package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(Model model) {
        // Obținem userul curent autentificat
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        // Trimitem mesaj de bun venit
        if (user != null) {
            model.addAttribute("welcomeMessage",
                    "Bun venit, " + user.getUsername() + " (" + user.getRole() + ")");
        } else {
            model.addAttribute("welcomeMessage", "Bun venit în aplicația Library Management!");
        }

        return "home"; // va afișa home.html din /templates
    }
}
