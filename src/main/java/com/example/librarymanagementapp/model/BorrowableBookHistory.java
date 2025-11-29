package com.example.librarymanagementapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrowable_book_history")
public class BorrowableBookHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private int year;
    private String category;
    private int quantity;
    private int borrowed;

    private String action;
    private LocalDateTime date;

    public BorrowableBookHistory() {}

    public BorrowableBookHistory(BorrowableBook book, String action) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.year = book.getYear();
        this.category = book.getCategory();
        this.quantity = book.getQuantity();
        this.borrowed = book.getBorrowed();
        this.action = action;
        this.date = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public int getBorrowed() { return borrowed; }
    public String getAction() { return action; }
    public LocalDateTime getDate() { return date; }
}
