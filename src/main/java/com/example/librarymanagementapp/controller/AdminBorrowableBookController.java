package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.BorrowableBook;
import com.example.librarymanagementapp.repository.BorrowableBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/borrowable")
public class AdminBorrowableBookController {

    @Autowired
    private BorrowableBookRepository borrowRepo;

    // ============================================
    // LIST BOOKS GROUPED BY CATEGORY
    // ============================================
    @GetMapping
    public String listBorrowable(Model model) {

        var books = borrowRepo.findAll();

        var grouped = books.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        BorrowableBook::getCategory,
                        java.util.LinkedHashMap::new,
                        java.util.stream.Collectors.toList()
                ));

        model.addAttribute("groupedBooks", grouped);
        return "admin_borrowable_books";
    }

    // ============================================
    // ADD FORM
    // ============================================
    @GetMapping("/add")
    public String addBorrowForm(@RequestParam(required = false) String category,
                                Model model) {

        BorrowableBook book = new BorrowableBook();

        if (category != null)
            book.setCategory(category);

        model.addAttribute("book", book);
        return "add_borrowable_book";
    }

    // ============================================
    // SAVE NEW BOOK
    // ============================================
    @PostMapping("/add")
    public String saveBorrowable(@ModelAttribute BorrowableBook book) {

        if (book.getQuantity() <= 0)
            book.setQuantity(20);

        if (book.getBorrowed() < 0)
            book.setBorrowed(0);

        borrowRepo.save(book);
        return "redirect:/admin/borrowable";
    }

    // ============================================
    // EDIT FORM
    // ============================================
    @GetMapping("/edit/{id}")
    public String editBorrowForm(@PathVariable Long id, Model model) {

        BorrowableBook b = borrowRepo.findById(id).orElse(null);
        if (b == null) return "redirect:/admin/borrowable";

        model.addAttribute("book", b);
        return "edit_borrowable_book";
    }

    // ============================================
    // SAVE EDIT
    // ============================================
    @PostMapping("/edit/{id}")
    public String updateBorrowable(@PathVariable Long id,
                                   @ModelAttribute BorrowableBook updated) {

        BorrowableBook book = borrowRepo.findById(id).orElse(null);
        if (book == null) return "redirect:/admin/borrowable";

        book.setTitle(updated.getTitle());
        book.setAuthor(updated.getAuthor());
        book.setYear(updated.getYear());
        book.setCategory(updated.getCategory());
        book.setQuantity(updated.getQuantity());
        book.setBorrowed(updated.getBorrowed());

        borrowRepo.save(book);
        return "redirect:/admin/borrowable";
    }

    // ============================================
    // DELETE
    // ============================================
    @GetMapping("/delete/{id}")
    public String deleteBorrowable(@PathVariable Long id) {

        borrowRepo.deleteById(id);
        return "redirect:/admin/borrowable";
    }
}
