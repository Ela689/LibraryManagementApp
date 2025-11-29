package com.example.librarymanagementapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "borrowable_books")
public class BorrowableBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private int year;
    private String category;

    private int quantity;   // total exemplare
    private int borrowed;   // câte sunt împrumutate

    public BorrowableBook() {}

    public BorrowableBook(String title, String author, int year, String category, int quantity, int borrowed) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.category = category;
        this.quantity = quantity;
        this.borrowed = borrowed;
    }

    // GETTERS & SETTERS
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getBorrowed() { return borrowed; }
    public void setBorrowed(int borrowed) { this.borrowed = borrowed; }
}
