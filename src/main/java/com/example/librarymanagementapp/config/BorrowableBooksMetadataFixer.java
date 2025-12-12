package com.example.librarymanagementapp.config;

import com.example.librarymanagementapp.model.BorrowableBook;
import com.example.librarymanagementapp.repository.BorrowableBookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BorrowableBooksMetadataFixer implements CommandLineRunner {

    private final BorrowableBookRepository repo;

    public BorrowableBooksMetadataFixer(BorrowableBookRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {

        for (BorrowableBook b : repo.findAll()) {

            boolean updated = false;

            if (b.getIsbn() == null || b.getIsbn().isEmpty()) {
                b.setIsbn("978-" + (100000000 + b.getId()));
                updated = true;
            }

            if (b.getEdition() == null) {
                b.setEdition("First Edition");
                updated = true;
            }

            if (b.getPages() == null || b.getPages() == 0) {
                b.setPages(180 + (int)(Math.random() * 120)); // între 180–300
                updated = true;
            }

            if (b.getPublisher() == null) {
                b.setPublisher("Classic Publishing House");
                updated = true;
            }

            if (b.getRelease_year() == null || b.getRelease_year() == 0) {
                b.setRelease_year(b.getYear()); // dacă nu există, folosește anul original
                updated = true;
            }

            if (b.getFormat() == null) {
                b.setFormat("Paperback");
                updated = true;
            }

            if (b.getCollection() == null) {
                b.setCollection("General Collection");
                updated = true;
            }

            if (b.getCover_type() == null) {
                b.setCover_type("Softcover");
                updated = true;
            }

            if (b.getTranslator() == null) {
                b.setTranslator("—");
                updated = true;
            }

            if (updated) repo.save(b);
        }

        System.out.println("✔ BorrowableBooksMetadataFixer: Missing metadata updated.");
    }
}
