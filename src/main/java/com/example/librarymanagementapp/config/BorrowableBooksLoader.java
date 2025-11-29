package com.example.librarymanagementapp.config;

import com.example.librarymanagementapp.model.BorrowableBook;
import com.example.librarymanagementapp.repository.BorrowableBookRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

@Component
public class BorrowableBooksLoader {

    private final BorrowableBookRepository repo;

    public BorrowableBooksLoader(BorrowableBookRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void loadBorrowableBooks() {
        try {
            if (repo.count() > 0) return;

            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getClassLoader().getResourceAsStream("borrowable_books.json");
            List<BorrowableBook> list = mapper.readValue(is, new TypeReference<List<BorrowableBook>>() {});

            repo.saveAll(list);
            System.out.println("📘 Loaded borrowable_books.json successfully!");

        } catch (Exception e) {
            System.out.println("❌ Error loading borrowable books: " + e.getMessage());
        }
    }
}
