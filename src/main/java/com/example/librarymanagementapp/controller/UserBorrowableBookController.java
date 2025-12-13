package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.BorrowableBook;
import com.example.librarymanagementapp.model.BorrowedBook;
import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.repository.BorrowableBookRepository;
import com.example.librarymanagementapp.repository.BorrowedBookRepository;
import com.example.librarymanagementapp.repository.UserRepository;
import com.example.librarymanagementapp.service.BorrowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/borrowable")
public class UserBorrowableBookController {

    @Autowired
    private BorrowableBookRepository borrowableRepo;

    @Autowired
    private BorrowedBookRepository borrowedRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BorrowService borrowService;

    // ============================================
    // 📚 LISTĂ CĂRȚI DISPONIBILE PENTRU IMPRUMUT
    // ============================================
    @GetMapping
    public String listBorrowableBooks(Model model) {

        List<BorrowableBook> books = borrowableRepo.findAll();
        model.addAttribute("books", books);

        return "user_borrowable_books"; // ⚠️ trebuie să existe HTML-ul
    }

    // ============================================
    // ➕ BORROW BOOK (MAXIM 1 EXEMPLAR / USER)
    // ============================================
    @PostMapping("/borrow/{bookId}")
    public String borrowBook(@PathVariable Long bookId,
                             Authentication auth,
                             Model model) {

        // 🔥 FIX: findByUsername returnează User, NU Optional
        User user = userRepo.findByUsername(auth.getName());

        if (user == null) {
            return "redirect:/login";
        }

        String result = borrowService.borrowBook(user.getId(), bookId);

        if (!"SUCCESS".equals(result)) {
            model.addAttribute("error", result);
        }

        return "redirect:/user/borrowable";
    }

    // ============================================
    // 📜 ISTORIC IMPRUMUTURI USER
    // ============================================
    @GetMapping("/my-borrows")
    public String myBorrows(Authentication auth, Model model) {

        User user = userRepo.findByUsername(auth.getName());

        if (user == null) {
            return "redirect:/login";
        }

        List<BorrowedBook> borrows = borrowedRepo.findByUserId(user.getId());
        model.addAttribute("borrows", borrows);

        return "user_my_borrows";
    }

    // ============================================
    // 🔁 RETURN BOOK
    // ============================================
    @PostMapping("/return/{borrowId}")
    public String returnBook(@PathVariable Long borrowId) {

        borrowService.returnBook(borrowId);
        return "redirect:/user/borrowable/my-borrows";
    }
}
