package com.example.librarymanagementapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private BorrowableBook book;

    private LocalDate borrowDate;
    private LocalDate dueDate;

    private boolean returned = false;

    private int fine = 0;

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public BorrowableBook getBook() { return book; }
    public void setBook(BorrowableBook book) { this.book = book; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }

    public int getFine() { return fine; }
    public void setFine(int fine) { this.fine = fine; }
}
