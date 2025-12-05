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

    // HOME ADMIN PANEL (Main menu)
    @GetMapping("/home")
    public String adminHome() {
        return "admin_books";   // pagina Manage Books + meniurile
    }


    // USER LIST PAGE
    @GetMapping("/users")
    public String listUsers(Model model) {

        List<User> users = userRepository.findAll();

        List<User> adminUsers = users.stream()
                .filter(u -> "ADMIN".equalsIgnoreCase(u.getRole()))
                .toList();

        List<User> normalUsers = users.stream()
                .filter(u -> "USER".equalsIgnoreCase(u.getRole()))
                .toList();

        model.addAttribute("adminUsers", adminUsers);
        model.addAttribute("normalUsers", normalUsers);
        return "admin_users";
    }

    @GetMapping("/users/view/{id}")
    public String viewUserDetails(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin_user_details";
    }

    @PostMapping("/users/deactivate/{id}")
    public String toggleUserStatus(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            user.setActive(!user.isActive());
            userRepository.save(user);
        }
        return "redirect:/admin/users/view/" + id;
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            userRepository.delete(user);
        }
        return "redirect:/admin/users";
    }
}
