package com.example.librarymanagementapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private int year;

    private String category; // Art, Medicine, Science, etc.

    private String edition;  // PHYSICAL sau BORROWABLE

    private int quantity;

    private int borrowed;

    private String isbn;

    private String format;

    private String filePath;

    public Book() {}

    public Book(String title, String author, int year, String category, String edition,
                int quantity, int borrowed, String isbn, String format, String filePath) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.category = category;
        this.edition = edition;
        this.quantity = quantity;
        this.borrowed = borrowed;
        this.isbn = isbn;
        this.format = format;
        this.filePath = filePath;
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

    public String getEdition() { return edition; }

    public void setEdition(String edition) { this.edition = edition; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getBorrowed() { return borrowed; }

    public void setBorrowed(int borrowed) { this.borrowed = borrowed; }

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getFormat() { return format; }

    public void setFormat(String format) { this.format = format; }

    public String getFilePath() { return filePath; }

    public void setFilePath(String filePath) { this.filePath = filePath; }
}
