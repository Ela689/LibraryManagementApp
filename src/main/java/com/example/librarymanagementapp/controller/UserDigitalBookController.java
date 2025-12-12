package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.DigitalBook;
import com.example.librarymanagementapp.model.DigitalBookHistory;
import com.example.librarymanagementapp.repository.DigitalBookHistoryRepository;
import com.example.librarymanagementapp.repository.DigitalBookRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/books/digital")
public class UserDigitalBookController {

    @Autowired
    private DigitalBookRepository digitalRepo;

    @Autowired
    private DigitalBookHistoryRepository historyRepo;


    /** LISTARE + SORTARE */
    @GetMapping
    public String listDigitalBooks(
            @RequestParam(required = false) String sort,
            Model model) {

        List<DigitalBook> books = digitalRepo.findAll();

        if (sort != null) {
            books.sort((a, b) -> switch (sort) {
                case "author" -> a.getAuthor().compareToIgnoreCase(b.getAuthor());
                case "year" -> Integer.compare(a.getYear(), b.getYear());
                default -> a.getTitle().compareToIgnoreCase(b.getTitle());
            });
        }

        Map<String, List<DigitalBook>> grouped =
                books.stream()
                        .collect(Collectors.groupingBy(DigitalBook::getCategory));

        model.addAttribute("groupedBooks", grouped);
        return "user_books_digital";
    }


    /** CĂUTARE FIZICĂ A FIȘIERULUI */
    private File resolveFile(String fileName) {
        String base = "src/main/resources/static/books/";

        String[] categories = {
                "art", "science", "philosophy",
                "fiction-romance-thriller", "medicine", "technology"
        };

        for (String cat : categories) {
            File f = Paths.get(base, cat, fileName).toFile();
            if (f.exists()) return f;
        }

        return null;
    }


    /** DOWNLOAD */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {

        File file = resolveFile(fileName);

        if (file == null) {
            response.sendError(404);
            return;
        }

        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setContentType("application/pdf");

        try (InputStream in = new FileInputStream(file)) {
            in.transferTo(response.getOutputStream());
        }
    }


    /** VIEW + SALVARE ÎN ISTORIC */
    @GetMapping("/view/{fileName}")
    public void view(@PathVariable String fileName, HttpServletResponse response) throws IOException {

        File file = resolveFile(fileName);

        if (file == null) {
            response.sendError(404);
            return;
        }

        // găsim cartea ca să o salvăm în istoric
        digitalRepo.findAll().stream()
                .filter(b -> b.getFileName().equals(fileName))
                .findFirst()
                .ifPresent(book -> historyRepo.save(new DigitalBookHistory(book, "VIEWED")));

        response.setContentType("application/pdf");
        try (InputStream in = new FileInputStream(file)) {
            in.transferTo(response.getOutputStream());
        }
    }
}
