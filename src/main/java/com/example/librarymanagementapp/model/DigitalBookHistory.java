package com.example.librarymanagementapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "digital_books_history")
public class DigitalBookHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action; // DELETED / UPDATED / etc.

    private String title;
    private String author;
    private int year;
    private String category;
    private String fileName;

    private LocalDateTime date;

    public DigitalBookHistory() {}

    public DigitalBookHistory(DigitalBook book, String action) {
        this.action = action;

        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.year = book.getYear();
        this.category = book.getCategory();
        this.fileName = book.getFileName();

        this.date = LocalDateTime.now();
    }

    // GETTERS
    public Long getId() { return id; }
    public String getAction() { return action; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public String getCategory() { return category; }
    public String getFileName() { return fileName; }
    public LocalDateTime getDate() { return date; }
}
