package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.BorrowedBook;
import com.example.librarymanagementapp.repository.BorrowedBookRepository;
import com.example.librarymanagementapp.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/borrows")
public class AdminBorrowManagementController {

    @Autowired
    private BorrowedBookRepository borrowedRepo;

    @Autowired
    private BorrowService borrowService;

    // ===============================
    // 📋 LIST ALL BORROWS
    // ===============================
    @GetMapping
    public String viewBorrows(Model model) {
        model.addAttribute("borrows", borrowedRepo.findAll());
        return "admin_borrow_management";
    }

    // ===============================
    // ✅ MARK AS RETURNED (ADMIN ONLY)
    // ===============================
    @PostMapping("/return/{id}")
    public String markReturned(@PathVariable Long id) {
        borrowService.returnBook(id);
        return "redirect:/admin/borrows";
    }

    // ===============================
    // 🔄 RUN LATE CHECK
    // ===============================
    @PostMapping("/run-check")
    public String runLateCheck() {
        borrowService.runLateCheck();
        return "redirect:/admin/borrows";
    }

    // ===============================
    // 💸 RESET LATE FEE
    // ===============================
    @PostMapping("/reset/{id}")
    public String resetFee(@PathVariable Long id) {
        borrowService.resetLateFee(id);
        return "redirect:/admin/borrows";
    }

    // ===============================
    // ❌ DELETE BORROW
    // ===============================
    @PostMapping("/delete/{id}")
    public String deleteBorrow(@PathVariable Long id) {
        borrowService.adminDeleteBorrow(id);
        return "redirect:/admin/borrows";
    }
}
