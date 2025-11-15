package com.example.librarymanagementapp;

import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.model.Book;
import com.example.librarymanagementapp.model.Category;
import com.example.librarymanagementapp.repository.UserRepository;
import com.example.librarymanagementapp.repository.BookRepository;
import com.example.librarymanagementapp.repository.CategoryRepository;
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

        // ✅ Create default admin if not exists
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // 🔐 Encrypt password
            admin.setEmail("admin@library.com");
            admin.setPhone("0700000000");
            admin.setRole("ADMIN");
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("✅ Admin user created: username=admin, password=admin123");
        } else {
            System.out.println("ℹ️ Admin user already exists.");
        }

        // ✅ Create default category if none exists
        if (categoryRepository.count() == 0) {
            Category fiction = new Category();
            fiction.setName("Fiction");
            fiction.setDescription("Fictional books category");
            categoryRepository.save(fiction);
            System.out.println("📚 Default category 'Fiction' created!");
        }

        // ✅ Create default book if none exists
        if (bookRepository.count() == 0) {
            Category fiction = categoryRepository.findByName("Fiction");
            if (fiction != null) {
                Book book = new Book();
                book.setTitle("The Great Gatsby");
                book.setAuthor("F. Scott Fitzgerald");
                book.setIsbn("9780743273565");
                book.setPrice(49.99);
                book.setAvailable(true);
                book.setCategory(fiction);
                bookRepository.save(book);
                System.out.println("📖 Default book 'The Great Gatsby' added!");
            }
        }

        System.out.println("✅ Library Management App is running...");
    }
}
