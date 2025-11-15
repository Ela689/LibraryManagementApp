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

    /**
     * Home page logic after login — dynamic based on role
     * ADMIN  → goes to admin dashboard (/home)
     * USER   → redirected to /user_wait (pending user panel)
     */
    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }

        // ✅ If it's a normal user → redirect to user panel
        if ("USER".equals(user.getRole())) {
            return "redirect:/user_wait";
        }

        // ✅ For ADMIN → load admin dashboard
        model.addAttribute("user", user);
        return "home";
    }

    /**
     * Optional: simple mapping for root "/"
     * Redirects directly to /home if authenticated
     */
    @GetMapping("/")
    public String rootRedirect() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return "redirect:/home";
        }
        return "redirect:/login";
    }
}
