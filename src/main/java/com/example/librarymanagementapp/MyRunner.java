package com.example.librarymanagementapp;

import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setEmail("admin@library.com");
            admin.setPhone("0700000000");
            admin.setRole("ADMIN");
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("✅ Admin user created!");
        } else {
            System.out.println("✅ Users already exist in DB.");
        }
    }
}
