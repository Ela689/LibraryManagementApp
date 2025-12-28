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

    // =====================================================
    //  IMPRUMUTAREA UNEI CARTI (1 EXEMPLAR / USER)
    // =====================================================
    public String borrowBook(Long userId, Long bookId) {

        User user = userRepo.findById(userId).orElse(null);
        BorrowableBook book = bookRepo.findById(bookId).orElse(null);

        if (user == null || book == null) {
            return "NOT_FOUND";
        }

        // 🔒 REGULA: user NU poate imprumuta aceeasi carte de 2 ori
        if (borrowedRepo.existsByUserIdAndBookIdAndReturnedFalse(userId, bookId)) {
            return "ALREADY_BORROWED";
        }

        // 📦 verificam stocul
        if (book.getQuantity() - book.getBorrowed() <= 0) {
            return "NO_COPIES";
        }

        // ➕ actualizam stocul
        book.setBorrowed(book.getBorrowed() + 1);
        bookRepo.save(book);

        BorrowedBook br = new BorrowedBook(
                user,
                book,
                LocalDate.now(),
                LocalDate.now().plusDays(14) // 14 zile
        );

        borrowedRepo.save(br);

        return "SUCCESS";
    }

    // =====================================================
    // RETURNAREA UNEI CARTI
    // =====================================================
    public String returnBook(Long borrowId) {

        BorrowedBook br = borrowedRepo.findById(borrowId).orElse(null);
        if (br == null) {
            return "NOT_FOUND";
        }

        br.setReturnDate(LocalDate.now());

        // calcul penalizare
        if (br.getReturnDate().isAfter(br.getDueDate())) {
            long daysLate =
                    ChronoUnit.DAYS.between(br.getDueDate(), br.getReturnDate());
            br.setLateFee(daysLate * 20.0); // 20 lei / zi
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

    // =====================================================
    // ADMIN – RESETARE PENALIZARE
    // =====================================================
    public String resetLateFee(Long borrowId) {

        BorrowedBook br = borrowedRepo.findById(borrowId).orElse(null);
        if (br == null) {
            return "NOT_FOUND";
        }

        br.setLateFee(0.0);
        borrowedRepo.save(br);

        return "RESET_OK";
    }

    // =====================================================
    // ADMIN – STERGERE COMPLETA IMPRUMUT
    // =====================================================
    public String adminDeleteBorrow(Long borrowId) {

        BorrowedBook br = borrowedRepo.findById(borrowId).orElse(null);
        if (br == null) {
            return "NOT_FOUND";
        }

        if (!br.isReturned()) {
            BorrowableBook bk = br.getBook();
            bk.setBorrowed(bk.getBorrowed() - 1);
            bookRepo.save(bk);
        }

        borrowedRepo.delete(br);
        return "DELETED";
    }

    // =====================================================
    // JOB – RECALCULARE PENALIZARI
    // =====================================================
    public void runLateCheck() {
        borrowedRepo.findByReturnedFalse()
                .forEach(borrow -> {
                    if (borrow.getDueDate().isBefore(LocalDate.now())) {
                        long daysLate = ChronoUnit.DAYS.between(
                                borrow.getDueDate(), LocalDate.now()
                        );
                        borrow.setLateFee(daysLate * 20.0);
                        borrowedRepo.save(borrow);
                    }
                });
    }
}
