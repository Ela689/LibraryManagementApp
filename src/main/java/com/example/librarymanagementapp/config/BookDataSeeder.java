package com.example.librarymanagementapp.config;

import com.example.librarymanagementapp.model.Book;
import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.repository.BookRepository;
import com.example.librarymanagementapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Configuration
public class BookDataSeeder implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // ============================
        // 👤 Seed admin account
        // ============================
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("✅ Default admin account created: username=admin, password=admin123");
        } else {
            System.out.println("ℹ️ Admin account already exists.");
        }

        // ============================
        // 📚 Seed books if empty
        // ============================
        if (bookRepository.count() > 0) {
            System.out.println("📚 Books already exist, skipping seeding...");
            return;
        }

        System.out.println("⚙️ Seeding initial books into the database...");

        List<String> categories = Arrays.asList(
                "Fiction", "Science", "Technology", "Philosophy", "Art",
                "History", "Psychology", "Education", "Business", "Health"
        );

        List<String> authors = Arrays.asList(
                "George Orwell", "Isaac Asimov", "Stephen King", "Jane Austen",
                "J.K. Rowling", "F. Scott Fitzgerald", "Ernest Hemingway",
                "Albert Camus", "Yuval Noah Harari", "Agatha Christie"
        );

        List<String> formats = Arrays.asList("Physical", "Borrowable", "Ebook");

        Random random = new Random();
        int idCounter = 1;

        for (int i = 0; i < 50; i++) {
            String category = categories.get(random.nextInt(categories.size()));
            String author = authors.get(random.nextInt(authors.size()));
            String format = formats.get(random.nextInt(formats.size()));

            String title = category + " Book " + idCounter;
            String edition = "Edition " + (random.nextInt(3) + 1);
            int year = 2000 + random.nextInt(25);
            int quantity = random.nextInt(20) + 1;
            int borrowed = format.equals("Borrowable") ? random.nextInt(quantity) : 0;
            boolean available = quantity > borrowed;

            String filePath = format.equals("Ebook")
                    ? "/uploads/ebooks/" + title.replace(" ", "_").toLowerCase() + ".pdf"
                    : null;

            double price = 20 + (random.nextInt(80)); // price between 20–100
            String isbn = "9780" + (100000000 + random.nextInt(900000000));

            Book book = new Book(
                    title,
                    author,
                    edition,
                    year,
                    category,
                    format,
                    quantity,
                    borrowed,
                    available,
                    filePath,
                    price,
                    isbn
            );

            bookRepository.save(book);
            idCounter++;
        }

        System.out.println("✅ Successfully seeded 50 books into the database!");
    }
}
