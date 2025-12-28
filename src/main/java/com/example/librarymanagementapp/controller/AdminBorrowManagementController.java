package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.BorrowedBook;
import com.example.librarymanagementapp.repository.BorrowedBookRepository;
import com.example.librarymanagementapp.service.BorrowService;
import com.example.librarymanagementapp.service.EmailService;

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

    @Autowired
    private EmailService emailService;

    // ===============================
    // 📋 LIST ALL BORROWS
    // ===============================
    @GetMapping
    public String viewBorrows(Model model) {
        model.addAttribute("borrows", borrowedRepo.findAll());
        return "admin_borrow_management";
    }

    // ===============================
    // ✅ MARK AS RETURNED
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

    // ==================================================
    // 📧 OPEN SEND NOTIFICATION FORM
    // ==================================================
    @GetMapping("/notify/{id}")
    public String openNotificationForm(@PathVariable Long id, Model model) {

        BorrowedBook borrow = borrowedRepo.findById(id).orElse(null);
        if (borrow == null) {
            return "redirect:/admin/borrows";
        }

        model.addAttribute("borrow", borrow);
        model.addAttribute("email", borrow.getUser().getEmail());
        model.addAttribute("username", borrow.getUser().getUsername());
        model.addAttribute("bookTitle", borrow.getBook().getTitle());
        model.addAttribute("dueDate", borrow.getDueDate().toString());

        return "admin_send_notification";
    }

    // ==================================================
    // 📤 SEND EMAIL NOTIFICATION
    // ==================================================
    @PostMapping("/notify/send")
    public String sendNotification(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String message
    ) {

        emailService.sendEmail(to, subject, message);
        return "redirect:/admin/borrows";
    }
}
