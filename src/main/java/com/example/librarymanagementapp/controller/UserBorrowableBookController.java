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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // ==================================================
    // 📚 LISTĂ CĂRȚI DISPONIBILE (GROUPATE PE CATEGORII)
    // + ID-urile cărților deja împrumutate de user
    // ==================================================
    @GetMapping
    public String listBorrowableBooks(Authentication auth, Model model) {

        List<BorrowableBook> books = borrowableRepo.findAll();

        Map<String, List<BorrowableBook>> groupedBooks =
                books.stream().collect(
                        Collectors.groupingBy(
                                BorrowableBook::getCategory,
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                );

        model.addAttribute("groupedBooks", groupedBooks);

        // 🔹 Cărți deja împrumutate de user (pentru sticker + disable)
        if (auth != null) {
            User user = userRepo.findByUsername(auth.getName());
            if (user != null) {

                List<Long> borrowedBookIds =
                        borrowedRepo.findByUserAndReturnedFalse(user)
                                .stream()
                                .map(b -> b.getBook().getId())
                                .toList();

                model.addAttribute("borrowedBookIds", borrowedBookIds);
            }
        }

        return "user_books_borrowable";
    }

    // ==================================================
    // ➕ BORROW BOOK
    // ==================================================
    @PostMapping("/borrow/{bookId}")
    public String borrowBook(@PathVariable Long bookId, Authentication auth) {

        User user = userRepo.findByUsername(auth.getName());
        if (user == null) {
            return "redirect:/login";
        }

        borrowService.borrowBook(user.getId(), bookId);
        return "redirect:/user/borrowable";
    }

    // ==================================================
    // 📜 ISTORIC IMPRUMUTURI USER
    // ==================================================
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

    // ==================================================
    // 🔁 RETURN BOOK
    // ==================================================
    @PostMapping("/return/{borrowId}")
    public String returnBook(@PathVariable Long borrowId) {

        borrowService.returnBook(borrowId);
        return "redirect:/user/borrowable/my-borrows";
    }

    // ==================================================
    // 📖 DETAILS – BORROWABLE BOOK (USER)
    // ==================================================
    @GetMapping("/details/{id}")
    public String borrowableDetails(@PathVariable Long id, Model model) {

        BorrowableBook book = borrowableRepo.findById(id).orElse(null);
        if (book == null) {
            return "redirect:/user/borrowable";
        }

        model.addAttribute("book", book);
        return "user_borrowable_book_details";
    }
}
