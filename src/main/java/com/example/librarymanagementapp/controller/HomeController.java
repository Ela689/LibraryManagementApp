//HomeController.java
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }

        if ("USER".equals(user.getRole())) {
            return "redirect:/user_wait";
        }

        model.addAttribute("user", user);
        return "admin_home";
    }

    @GetMapping("/")
    public String rootRedirect() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return "redirect:/home";
        }
        return "redirect:/login";
    }
}
