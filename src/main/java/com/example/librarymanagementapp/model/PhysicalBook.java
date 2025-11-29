package com.example.librarymanagementapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "physical_books")
public class PhysicalBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private int year;
    private String category;

    private int quantity;

    public PhysicalBook() {}

    public PhysicalBook(String title, String author, int year, String category, int quantity) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.category = category;
        this.quantity = quantity;
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
}
