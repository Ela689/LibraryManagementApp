package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.PhysicalBook;
import com.example.librarymanagementapp.repository.PhysicalBookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/books/physical")
public class UserPhysicalBookController {

    private final PhysicalBookRepository repo;

    public UserPhysicalBookController(PhysicalBookRepository repo) {
        this.repo = repo;
    }

    // ============================
    // LIST PAGE
    // ============================
    @GetMapping
    public String listBooks(
            @RequestParam(required = false) String sort,
            Model model) {

        List<PhysicalBook> books = repo.findAll();

        if (sort != null) {
            switch (sort) {
                case "title" -> books.sort(Comparator.comparing(PhysicalBook::getTitle));
                case "author" -> books.sort(Comparator.comparing(PhysicalBook::getAuthor));
                case "year" -> books.sort(Comparator.comparingInt(PhysicalBook::getYear));
                case "quantity" -> books.sort(Comparator.comparingInt(PhysicalBook::getQuantity));
            }
        }

        Map<String, List<PhysicalBook>> groupedBooks =
                books.stream()
                        .collect(Collectors.groupingBy(
                                PhysicalBook::getCategory,
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        model.addAttribute("groupedBooks", groupedBooks);
        return "user_physical_books";
    }

    // ============================
    // DETAILS PAGE + HISTORY
    // ============================
    @GetMapping("/details/{id}")
    public String details(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        Optional<PhysicalBook> optionalBook = repo.findById(id);
        if (optionalBook.isEmpty()) {
            return "redirect:/user/books/physical";
        }

        PhysicalBook book = optionalBook.get();

        // 🔹 HISTORY (SESSION)
        List<PhysicalBook> history =
                (List<PhysicalBook>) session.getAttribute("physicalHistory");

        if (history == null) {
            history = new ArrayList<>();
        }

        history.removeIf(b -> b.getId().equals(book.getId()));
        history.add(0, book);

        if (history.size() > 10) {
            history = history.subList(0, 10);
        }

        session.setAttribute("physicalHistory", history);

        model.addAttribute("book", book);
        return "user_physical_details";
    }

    // ============================
    // HISTORY PAGE
    // ============================
    @GetMapping("/history")
    public String history(Model model, HttpSession session) {

        List<PhysicalBook> history =
                (List<PhysicalBook>) session.getAttribute("physicalHistory");

        if (history == null) {
            history = new ArrayList<>();
        }

        model.addAttribute("history", history);
        return "user_physical_history";
    }

    // ============================
// CLEAR HISTORY
// ============================
    @PostMapping("/history/clear")
    public String clearHistory(HttpSession session) {
        session.removeAttribute("physicalHistory");
        return "redirect:/user/books/physical/history";
    }

}
