package com.example.librarymanagementapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrowed_books")
public class BorrowedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // USER-ul care împrumută
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // CARTEA împrumutabilă
    @ManyToOne
    @JoinColumn(name = "book_id")
    private BorrowableBook book;

    private LocalDate borrowDate;   // data împrumutului
    private LocalDate dueDate;      // termen: azi + 20 zile
    private LocalDate returnDate;   // rămâne null până la retur

    private Double lateFee = 0.0;   // calculată automat
    private boolean returned = false; // status carte

    public BorrowedBook() {}

    public BorrowedBook(User user, BorrowableBook book, LocalDate borrowDate, LocalDate dueDate) {
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    // CALCULAM AUTOMAT AMENDA
    public void updateLateFee() {
        if (returnDate != null || LocalDate.now().isBefore(dueDate)) {
            lateFee = 0.0;
        } else {
            long daysLate = LocalDate.now().toEpochDay() - dueDate.toEpochDay();
            lateFee = daysLate * 20.0;
        }
    }

    // GETTERS & SETTERS

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BorrowableBook getBook() { return book; }
    public void setBook(BorrowableBook book) { this.book = book; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.returned = true;
    }

    public Double getLateFee() { return lateFee; }
    public void setLateFee(Double lateFee) { this.lateFee = lateFee; }

    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }
}
