//DigitalBooksLoader.java
package com.example.librarymanagementapp.config;

import com.example.librarymanagementapp.model.DigitalBook;
import com.example.librarymanagementapp.repository.DigitalBookRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

@Component
public class DigitalBooksLoader {

    @Autowired
    private DigitalBookRepository digitalBookRepository;

    private static final String BASE_PATH = "src/main/resources/static/books";

    @PostConstruct
    public void loadDigitalBooks() throws IOException {

        // Dacă baza de date are deja cărți, nu încărcăm iar
        if (digitalBookRepository.count() > 0) {
            System.out.println("Digital books already loaded.");
            return;
        }

        System.out.println("Loading digital books automatically from folders...");

        // parcurgem fiecare subfolder
        try (Stream<Path> categories = Files.list(Paths.get(BASE_PATH))) {
            categories
                    .filter(Files::isDirectory)
                    .forEach(categoryDir -> {
                        String category = categoryDir.getFileName().toString();

                        try (Stream<Path> files = Files.list(categoryDir)) {
                            files
                                    .filter(f -> f.toString().endsWith(".pdf"))
                                    .forEach(pdf -> {
                                        try {
                                            String fileName = pdf.getFileName().toString();

                                            // extragem titlu / autor / an din numele fișierului
                                            String[] parts = fileName.replace(".pdf", "").split(" - ");

                                            String title = parts.length > 0 ? parts[0] : "Unknown";
                                            String author = parts.length > 1 ? parts[1] : "Unknown";
                                            Integer year = parts.length > 2 ? extractYear(parts[2]) : 0;

                                            DigitalBook book = new DigitalBook();
                                            book.setTitle(title.trim());
                                            book.setAuthor(author.trim());
                                            book.setYear(year);
                                            book.setCategory(category);
                                            book.setFileName(category + "/" + fileName);

                                            digitalBookRepository.save(book);

                                        } catch (Exception e) {
                                            System.out.println("Error loading file: " + pdf + " -> " + e.getMessage());
                                        }
                                    });

                        } catch (Exception e) {
                            System.out.println("Error reading folder: " + categoryDir);
                        }
                    });
        }

        System.out.println("All digital books loaded successfully.");
    }

    private Integer extractYear(String text) {
        try {
            return Integer.parseInt(text.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}
