package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.BorrowableBook;
import com.example.librarymanagementapp.model.BorrowableBookHistory;
import com.example.librarymanagementapp.repository.BorrowableBookRepository;
import com.example.librarymanagementapp.repository.BorrowableBookHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/borrowable")
public class AdminBorrowableBookController {

    @Autowired
    private BorrowableBookRepository borrowRepo;

    @Autowired
    private BorrowableBookHistoryRepository historyRepo;

    // ============================================
    // LIST GROUPED
    // ============================================
    @GetMapping
    public String listBorrowable(Model model) {

        List<BorrowableBook> books = borrowRepo.findAll();

        Map<String, List<BorrowableBook>> grouped = new LinkedHashMap<>();
        for (BorrowableBook b : books) {
            grouped.computeIfAbsent(b.getCategory(), c -> new ArrayList<>()).add(b);
        }

        // 🔥 FIX: trimitem groupedBooks ca în celelalte pagini
        model.addAttribute("groupedBooks", grouped);

        return "admin_borrowable_books";
    }

    // ============================================
    // ADD FORM
    // ============================================
    @GetMapping("/add")
    public String addForm(@RequestParam(required = false) String category, Model model) {

        BorrowableBook book = new BorrowableBook();
        if (category != null)
            book.setCategory(category);

        model.addAttribute("book", book);
        return "add_borrowable_book";
    }

    // ============================================
    // SAVE NEW
    // ============================================
    @PostMapping("/add")
    public String saveBorrowable(@ModelAttribute BorrowableBook book) {

        if (book.getQuantity() <= 0) book.setQuantity(20);
        if (book.getBorrowed() < 0) book.setBorrowed(0);

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
    // DELETE → SAVE HISTORY
    // ============================================
    @GetMapping("/delete/{id}")
    public String deleteBorrowable(@PathVariable Long id) {

        BorrowableBook book = borrowRepo.findById(id).orElse(null);
        if (book != null) {
            historyRepo.save(new BorrowableBookHistory(book, "DELETED"));
            borrowRepo.delete(book);
        }

        return "redirect:/admin/borrowable";
    }

    // ============================================
    // HISTORY PAGE
    // ============================================
    @GetMapping("/history")
    public String borrowableHistory(Model model) {
        model.addAttribute("history", historyRepo.findAll());
        return "history_borrowable_books";
    }

    // ============================================
    // RESTORE BOOK
    // ============================================
// ============================================
// RESTORE BOOK (VERSIONA CORECTĂ PENTRU NOILE CÂMPURI)
// ============================================
    @GetMapping("/restore/{historyId}")
    public String restoreBorrowable(@PathVariable Long historyId) {

        BorrowableBookHistory h = historyRepo.findById(historyId).orElse(null);

        if (h != null) {

            BorrowableBook restored = new BorrowableBook();

            // ---- câmpurile originale ----
            restored.setTitle(h.getTitle());
            restored.setAuthor(h.getAuthor());
            restored.setYear(h.getYear());
            restored.setCategory(h.getCategory());
            restored.setQuantity(h.getQuantity());
            restored.setBorrowed(h.getBorrowed());

            // ---- câmpurile noi adăugate în DB ----
            restored.setCollection(h.getCollection());
            restored.setCover_type(h.getCover_type());
            restored.setEdition(h.getEdition());
            restored.setFormat(h.getFormat());
            restored.setIsbn(h.getIsbn());
            restored.setPages(h.getPages());
            restored.setPublisher(h.getPublisher());
            restored.setRelease_year(h.getRelease_year());
            restored.setTranslator(h.getTranslator());

            borrowRepo.save(restored);

            // ștergem intrarea din istoric
            historyRepo.delete(h);
        }

        return "redirect:/admin/borrowable/history";
    }


    // ============================================
    // CLEAR HISTORY
    // ============================================
    @GetMapping("/history/clear")
    public String clearHistory() {
        historyRepo.deleteAll();
        return "redirect:/admin/borrowable/history";
    }
}
