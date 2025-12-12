package com.example.librarymanagementapp.service;

import com.example.librarymanagementapp.model.BorrowableBook;
import com.example.librarymanagementapp.model.BorrowedBook;
import com.example.librarymanagementapp.model.User;
import com.example.librarymanagementapp.repository.BorrowableBookRepository;
import com.example.librarymanagementapp.repository.BorrowedBookRepository;
import com.example.librarymanagementapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class BorrowService {

    @Autowired private UserRepository userRepo;
    @Autowired private BorrowableBookRepository bookRepo;
    @Autowired private BorrowedBookRepository borrowedRepo;

    // ===============================
    // 1️⃣ Imprumutarea unei carti
    // ===============================
    public String borrowBook(Long userId, Long bookId) {

        User user = userRepo.findById(userId).orElse(null);
        BorrowableBook book = bookRepo.findById(bookId).orElse(null);

        if (user == null || book == null) return "User or book not found";
        if (book.getQuantity() - book.getBorrowed() <= 0) return "No copies available";

        // actualizam stocul
        book.setBorrowed(book.getBorrowed() + 1);
        bookRepo.save(book);

        BorrowedBook br = new BorrowedBook(
                user,
                book,
                LocalDate.now(),
                LocalDate.now().plusDays(20)   // termen 20 zile
        );

        borrowedRepo.save(br);

        return "SUCCESS";
    }

    // ===============================
    // 2️⃣ Returnarea unei carti
    // ===============================
    public String returnBook(Long borrowId) {

        BorrowedBook br = borrowedRepo.findById(borrowId).orElse(null);
        if (br == null) return "Borrow record not found";

        // Marcam returnarea
        br.setReturnDate(LocalDate.now());

        // Calculam penalizarea (daca exista intarziere):
        if (br.getReturnDate().isAfter(br.getDueDate())) {

            long daysLate = ChronoUnit.DAYS.between(br.getDueDate(), br.getReturnDate());

            br.setLateFee(daysLate * 20.0); // 20 lei pe zi, DOUBLE FIXED
        } else {
            br.setLateFee(0.0);
        }

        br.setReturned(true);
        borrowedRepo.save(br);

        // refacem stocul cartii
        BorrowableBook book = br.getBook();
        book.setBorrowed(book.getBorrowed() - 1);
        bookRepo.save(book);

        return "SUCCESS";
    }

    // ===============================
    // 3️⃣ Stergere / Resetare penalizare de catre ADMIN
    // ===============================
    public String resetLateFee(Long borrowId) {
        BorrowedBook br = borrowedRepo.findById(borrowId).orElse(null);
        if (br == null) return "Not found";

        br.setLateFee(0.0);
        borrowedRepo.save(br);

        return "RESET_OK";
    }

    // ===============================
    // 4️⃣ ADMIN sterge complet un imprumut
    // ===============================
    public String adminDeleteBorrow(Long borrowId) {

        BorrowedBook br = borrowedRepo.findById(borrowId).orElse(null);
        if (br == null) return "Not found";

        // reactivam stocul, daca cartea nu returnata
        if (!br.isReturned()) {
            BorrowableBook bk = br.getBook();
            bk.setBorrowed(bk.getBorrowed() - 1);
            bookRepo.save(bk);
        }

        borrowedRepo.delete(br);
        return "DELETED";
    }
}
