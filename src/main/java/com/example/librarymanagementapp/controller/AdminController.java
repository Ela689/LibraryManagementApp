package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> pendingUsers = userRepository.findByActiveFalse();
        List<User> activeUsers = userRepository.findByActiveTrue();

        model.addAttribute("pendingUsers", pendingUsers);
        model.addAttribute("activeUsers", activeUsers);
        return "admin_users";
    }

    @PostMapping("/activate/{id}")
    public String activateUser(@PathVariable Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setActive(true);
            userRepository.save(user);
        });
        return "redirect:/admin/users";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivateUser(@PathVariable Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setActive(false);
            userRepository.save(user);
        });
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    // ✅ Download ID Card
    @GetMapping("/download/{id}")
    @ResponseBody
    public Resource downloadIdCard(@PathVariable Long id) throws MalformedURLException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null || user.getIdCardFilePath() == null) {
            return null;
        }

        Path path = Paths.get(user.getIdCardFilePath());
        return new UrlResource(path.toUri());
    }
}
