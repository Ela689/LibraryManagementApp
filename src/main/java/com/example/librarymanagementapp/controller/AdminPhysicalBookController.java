package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.PhysicalBook;
import com.example.librarymanagementapp.model.PhysicalBookHistory;
import com.example.librarymanagementapp.repository.PhysicalBookHistoryRepository;
import com.example.librarymanagementapp.repository.PhysicalBookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/physical")
public class AdminPhysicalBookController {

    @Autowired
    private PhysicalBookRepository physicalRepo;

    @Autowired
    private PhysicalBookHistoryRepository historyRepo;

    // ============================================
    // LIST - GROUPED BY CATEGORY
    // ============================================
    @GetMapping
    public String listPhysical(Model model) {

        List<PhysicalBook> books = physicalRepo.findAll();

        Map<String, List<PhysicalBook>> grouped = new LinkedHashMap<>();
        for (PhysicalBook b : books) {
            grouped.computeIfAbsent(b.getCategory(), c -> new ArrayList<>()).add(b);
        }

        // 🔥 FIX: trimitem groupedBooks ca și în DigitalBooks
        model.addAttribute("groupedBooks", grouped);

        return "admin_physical_books";
    }

    // ============================================
    // ADD FORM
    // ============================================
    @GetMapping("/add")
    public String addPhysicalForm(@RequestParam(required = false) String category, Model model) {

        PhysicalBook book = new PhysicalBook();
        if (category != null)
            book.setCategory(category);

        model.addAttribute("book", book);
        return "add_physical_book";
    }

    // ============================================
    // SAVE NEW
    // ============================================
    @PostMapping("/add")
    public String savePhysical(@ModelAttribute PhysicalBook book) {

        if (book.getQuantity() <= 0) book.setQuantity(50);

        physicalRepo.save(book);
        return "redirect:/admin/physical";
    }

    // ============================================
    // EDIT FORM
    // ============================================
    @GetMapping("/edit/{id}")
    public String editPhysicalForm(@PathVariable Long id, Model model) {

        PhysicalBook b = physicalRepo.findById(id).orElse(null);
        if (b == null) return "redirect:/admin/physical";

        model.addAttribute("book", b);
        return "edit_physical_book";
    }

    // ============================================
    // SAVE EDIT
    // ============================================
    @PostMapping("/edit/{id}")
    public String updatePhysical(@PathVariable Long id,
                                 @ModelAttribute PhysicalBook updated) {

        PhysicalBook book = physicalRepo.findById(id).orElse(null);
        if (book == null) return "redirect:/admin/physical";

        book.setTitle(updated.getTitle());
        book.setAuthor(updated.getAuthor());
        book.setYear(updated.getYear());
        book.setCategory(updated.getCategory());
        book.setQuantity(updated.getQuantity());

        physicalRepo.save(book);
        return "redirect:/admin/physical";
    }

    // ============================================
    // DELETE → SAVE HISTORY
    // ============================================
    @GetMapping("/delete/{id}")
    public String deletePhysical(@PathVariable Long id) {

        PhysicalBook book = physicalRepo.findById(id).orElse(null);
        if (book != null) {
            historyRepo.save(new PhysicalBookHistory(book, "DELETED"));
            physicalRepo.delete(book);
        }

        return "redirect:/admin/physical";
    }

    // ============================================
    // HISTORY PAGE
    // ============================================
    @GetMapping("/history")
    public String physicalHistory(Model model) {
        model.addAttribute("history", historyRepo.findAll());
        return "history_physical_books";
    }

    // ============================================
    // RESTORE BOOK
    // ============================================
    @GetMapping("/restore/{historyId}")
    public String restorePhysical(@PathVariable Long historyId) {

        PhysicalBookHistory h = historyRepo.findById(historyId).orElse(null);

        if (h != null) {
            PhysicalBook restored = new PhysicalBook(
                    h.getTitle(),
                    h.getAuthor(),
                    h.getYear(),
                    h.getCategory(),
                    h.getQuantity()
            );

            physicalRepo.save(restored);
            historyRepo.delete(h);
        }

        return "redirect:/admin/physical/history";
    }

    // ============================================
    // CLEAR HISTORY
    // ============================================
    @GetMapping("/history/clear")
    public String clearHistory() {
        historyRepo.deleteAll();
        return "redirect:/admin/physical/history";
    }
}
