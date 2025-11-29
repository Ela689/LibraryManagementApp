package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.PhysicalBook;
import com.example.librarymanagementapp.repository.PhysicalBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/physical")
public class AdminPhysicalBookController {

    @Autowired
    private PhysicalBookRepository physicalRepo;

    // ============================================
    // LIST ALL PHYSICAL BOOKS (GROUPED BY CATEGORY)
    // ============================================
    @GetMapping
    public String listPhysicalBooks(Model model) {

        var books = physicalRepo.findAll();

        // grupare pe categorii
        var grouped = books.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        PhysicalBook::getCategory,
                        java.util.LinkedHashMap::new,
                        java.util.stream.Collectors.toList()
                ));

        model.addAttribute("groupedBooks", grouped);
        return "admin_physical_books";
    }

    // ============================================
    // ADD BOOK FORM
    // ============================================
    @GetMapping("/add")
    public String addPhysicalForm(@RequestParam(required = false) String category,
                                  Model model) {

        PhysicalBook book = new PhysicalBook();
        if (category != null)
            book.setCategory(category);

        model.addAttribute("book", book);
        return "add_physical_book";
    }

    // ============================================
    // SAVE NEW BOOK
    // ============================================
    @PostMapping("/add")
    public String saveNewPhysical(@ModelAttribute PhysicalBook book) {

        if (book.getQuantity() <= 0)
            book.setQuantity(50); // valoare default

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
    // DELETE
    // ============================================
    @GetMapping("/delete/{id}")
    public String deletePhysical(@PathVariable Long id) {

        physicalRepo.deleteById(id);
        return "redirect:/admin/physical";
    }
}
