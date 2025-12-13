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
import java.util.List;

@Service
public class BorrowService {

    @Autowired private UserRepository userRepo;
    @Autowired private BorrowableBookRepository bookRepo;
    @Autowired private BorrowedBookRepository borrowedRepo;

    // =====================================================
    //  IMPRUMUTAREA UNEI CARTI (CU RESTRICTIE 1 / USER)
    // =====================================================
    public String borrowBook(Long userId, Long bookId) {

        User user = userRepo.findById(userId).orElse(null);
        BorrowableBook book = bookRepo.findById(bookId).orElse(null);

        if (user == null || book == null)
            return "User or book not found";

        if (borrowedRepo.existsByUserIdAndBookIdAndReturnedFalse(userId, bookId)) {
            return "ALREADY_BORROWED";
        }//adaugat recent

        // REGULĂ NOUĂ: user NU poate împrumuta aceeași carte de 2 ori
        boolean alreadyBorrowed =
                borrowedRepo.existsByUserIdAndBookIdAndReturnedFalse(userId, bookId);

        if (alreadyBorrowed)
            return "You already borrowed this book";

        // verificăm stocul
        if (book.getQuantity() - book.getBorrowed() <= 0)
            return "No copies available";

        // actualizam stocul
        book.setBorrowed(book.getBorrowed() + 1);
        bookRepo.save(book);

        BorrowedBook br = new BorrowedBook(
                user,
                book,
                LocalDate.now(),
                LocalDate.now().plusDays(14) // 🔔 14 zile (cum ai cerut)
        );

        borrowedRepo.save(br);

        return "SUCCESS";
    }


    // =====================================================
    // RETURNAREA UNEI CARTI
    // =====================================================
    public String returnBook(Long borrowId) {

        BorrowedBook br = borrowedRepo.findById(borrowId).orElse(null);
        if (br == null) return "Borrow record not found";

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
        if (br == null) return "Not found";

        br.setLateFee(0.0);
        borrowedRepo.save(br);

        return "RESET_OK";
    }

    // =====================================================
    // ADMIN – STERGERE COMPLETA IMPRUMUT
    // =====================================================
    public String adminDeleteBorrow(Long borrowId) {

        BorrowedBook br = borrowedRepo.findById(borrowId).orElse(null);
        if (br == null) return "Not found";

        if (!br.isReturned()) {
            BorrowableBook bk = br.getBook();
            bk.setBorrowed(bk.getBorrowed() - 1);
            bookRepo.save(bk);
        }

        borrowedRepo.delete(br);
        return "DELETED";
    }

    // =====================================================
    // METODA HELPER – RESTRICTIE 1 CARTE / USER
    // =====================================================
    private boolean userAlreadyBorrowedThisBook(User user, BorrowableBook book) {

        List<BorrowedBook> activeBorrows =
                borrowedRepo.findByUserAndReturnedFalse(user);

        return activeBorrows.stream()
                .anyMatch(b -> b.getBook().getId().equals(book.getId()));
    }
}
