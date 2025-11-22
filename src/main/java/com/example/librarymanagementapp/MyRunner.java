package com.example.librarymanagementapp;

import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.model.Category;
import com.example.librarymanagementapp.repository.UserRepository;
import com.example.librarymanagementapp.repository.CategoryRepository;
import com.example.librarymanagementapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("🚀 Checking initial data setup...");

        // ================================
        // 1️⃣ CREATE ADMIN IF MISSING
        // ================================
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@library.com");
            admin.setPhone("0700000000");
            admin.setRole("ADMIN");
            admin.setActive(true);
            userRepository.save(admin);

            System.out.println("✅ Admin user created.");
        } else {
            System.out.println("ℹ️ Admin user already exists.");
        }

        // ================================
        // 2️⃣ CREATE 6 DEFAULT CATEGORIES
        // ================================
        if (categoryRepository.count() == 0) {
            String[] categories = {
                    "Art",
                    "Fiction-Romance-Thriller",
                    "Medicine",
                    "Philosophy",
                    "Science",
                    "Technology"
            };

            for (String c : categories) {
                Category cat = new Category();
                cat.setName(c);
                categoryRepository.save(cat);
            }

            System.out.println("📚 Default 6 categories created.");
        } else {
            System.out.println("ℹ️ Categories already exist.");
        }

        // ================================
        // 3️⃣ DO NOT GENERATE BOOKS HERE
        // ================================
        System.out.println("📘 No auto-books inserted at startup.");
        System.out.println("✅ Library Management App is running...");
    }
}
