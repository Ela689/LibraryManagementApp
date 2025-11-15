package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // Pagina principală de gestionare a utilizatorilor
    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin_users";
    }

    // Activează un utilizator
    @PostMapping("/activate/{id}")
    public String activateUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    // Dezactivează un utilizator
    @PostMapping("/deactivate/{id}")
    public String deactivateUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setActive(false);
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    // Șterge un utilizator
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    // Pagina de gestionare a cărților (placeholder deocamdată)
    @GetMapping("/books")
    public String manageBooks() {
        return "admin_books";
    }
}
