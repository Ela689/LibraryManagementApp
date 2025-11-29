package com.example.librarymanagementapp.config;

import com.example.librarymanagementapp.model.PhysicalBook;
import com.example.librarymanagementapp.repository.PhysicalBookRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

@Component
public class PhysicalBooksLoader {

    private final PhysicalBookRepository repo;

    public PhysicalBooksLoader(PhysicalBookRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void loadPhysicalBooks() {
        try {
            if (repo.count() > 0) return;

            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getClassLoader().getResourceAsStream("physical_books.json");
            List<PhysicalBook> list = mapper.readValue(is, new TypeReference<List<PhysicalBook>>() {});

            repo.saveAll(list);
            System.out.println("📗 Loaded physical_books.json successfully!");

        } catch (Exception e) {
            System.out.println("❌ Error loading physical books: " + e.getMessage());
        }
    }
}
