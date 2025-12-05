package com.example.librarymanagementapp.service;

import com.example.librarymanagementapp.model.DigitalBook;
import com.example.librarymanagementapp.model.DigitalBookHistory;
import com.example.librarymanagementapp.repository.DigitalBookHistoryRepository;
import com.example.librarymanagementapp.repository.DigitalBookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DigitalBookService {

    private final DigitalBookRepository bookRepo;
    private final DigitalBookHistoryRepository historyRepo;

    private final String BASE_PATH = "src/main/resources/static/books/";

    public DigitalBookService(DigitalBookRepository bookRepo,
                              DigitalBookHistoryRepository historyRepo) {
        this.bookRepo = bookRepo;
        this.historyRepo = historyRepo;
    }

    // GROUP BY CATEGORY
    public Map<String, List<DigitalBook>> getBooksGrouped() {
        return bookRepo.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        DigitalBook::getCategory,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    // OPEN FILE PATH
    public Path getFilePath(DigitalBook book) {
        return Paths.get(BASE_PATH + book.getCategory() + "/" + book.getFileName());
    }

    // SAVE NEW BOOK
    public void saveBook(DigitalBook book, MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            String folder = BASE_PATH + book.getCategory();
            Files.createDirectories(Paths.get(folder));

            String clean = file.getOriginalFilename().replace(" ", "_");
            Path filePath = Paths.get(folder, clean);

            Files.write(filePath, file.getBytes());

            book.setFileName(clean);
        }

        bookRepo.save(book);
    }

    // UPDATE BOOK
    public void updateBook(Long id, DigitalBook updated, MultipartFile file) throws IOException {

        DigitalBook book = bookRepo.findById(id).orElse(null);
        if (book == null) return;

        book.setTitle(updated.getTitle());
        book.setAuthor(updated.getAuthor());
        book.setYear(updated.getYear());
        book.setCategory(updated.getCategory());

        if (file != null && !file.isEmpty()) {

            String folder = BASE_PATH + book.getCategory();
            Files.createDirectories(Paths.get(folder));

            String clean = file.getOriginalFilename().replace(" ", "_");
            Path filePath = Paths.get(folder, clean);

            Files.write(filePath, file.getBytes());

            book.setFileName(clean);
        }

        bookRepo.save(book);
    }

    // DELETE BOOK
    public void deleteBook(Long id) {

        DigitalBook book = bookRepo.findById(id).orElse(null);
        if (book != null) {

            historyRepo.save(new DigitalBookHistory(book, "DELETED"));

            bookRepo.delete(book);
        }
    }

    // RESTORE BOOK
    public void restore(Long historyId) {

        DigitalBookHistory h = historyRepo.findById(historyId).orElse(null);
        if (h != null) {

            DigitalBook restored = new DigitalBook(
                    h.getTitle(),
                    h.getAuthor(),
                    h.getYear(),
                    h.getCategory(),
                    h.getFileName()
            );

            bookRepo.save(restored);
            historyRepo.delete(h);
        }
    }
}
