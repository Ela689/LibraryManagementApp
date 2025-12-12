package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.BorrowableBook;
import com.example.librarymanagementapp.repository.BorrowableBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/books")
public class UserBooksController {

    @Autowired
    private BorrowableBookRepository borrowableRepo;

    // ===============================
    // BORROWABLE BOOKS
    // ===============================
    @GetMapping("/borrowable")
    public String viewBorrowableBooks(Model model) {
        List<BorrowableBook> allBooks = borrowableRepo.findAll();

        Map<String, List<BorrowableBook>> grouped =
                allBooks.stream()
                        .collect(Collectors.groupingBy(BorrowableBook::getCategory));

        model.addAttribute("groupedBooks", grouped);

        return "user_books_borrowable";
    }

    // ===============================
    // BOOK DETAILS
    // ===============================
    @GetMapping("/details/{id}")
    public String viewBookDetails(@PathVariable Long id, Model model) {
        BorrowableBook book = borrowableRepo.findById(id).orElse(null);
        model.addAttribute("book", book);
        return "user_book_details";
    }

    // ===============================
    // PHYSICAL BOOKS (STATIC LIST)
    // ===============================
    //@GetMapping("/physical")
    //public String viewPhysicalBooks() {
      //  return "user_books_physical";
    //}

    // ===============================
    // 🚫 !!! ATENȚIE !!!
    // Digital books sunt gestionate de UserDigitalBookController
    // NU mai punem niciun @GetMapping("/digital") aici!
    // ===============================

    // ===============================
    // BORROW ACTION
    // ===============================
    @PostMapping("/borrow/{id}")
    public String borrowBook(@PathVariable Long id) {

        BorrowableBook book = borrowableRepo.findById(id).orElse(null);

        if (book == null) {
            return "redirect:/user/books/borrowable";
        }

        int available = book.getQuantity() - book.getBorrowed();

        if (available > 0) {
            book.setBorrowed(book.getBorrowed() + 1);
            borrowableRepo.save(book);
        }

        return "redirect:/user/books/borrowable";
    }
}
