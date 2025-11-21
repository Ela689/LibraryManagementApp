package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // 🧑‍💻 Afișează toți utilizatorii
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> allUsers = userRepository.findAll();

        List<User> admins = allUsers.stream()
                .filter(u -> "ADMIN".equals(u.getRole()))
                .collect(Collectors.toList());

        List<User> normalUsers = allUsers.stream()
                .filter(u -> "USER".equals(u.getRole()))
                .collect(Collectors.toList());

        model.addAttribute("admins", admins);
        model.addAttribute("users", normalUsers);
        return "admin_users";
    }

    // 📄 Vizualizare detalii user
    @GetMapping("/users/view/{id}")
    public String viewUserDetails(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin_user_details";
    }

    // ✅ Activează / dezactivează cont
    @PostMapping("/users/toggle/{id}")
    public String toggleUserActive(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && !"ADMIN".equals(user.getRole())) {
            user.setActive(!user.isActive());
            userRepository.save(user);
        }
        return "redirect:/admin/users";
    }

    // 🗑️ Șterge userul
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && !"ADMIN".equals(user.getRole())) {
            userRepository.delete(user);
        }
        return "redirect:/admin/users";
    }
}
