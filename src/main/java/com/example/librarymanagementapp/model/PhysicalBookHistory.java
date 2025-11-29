package com.example.librarymanagementapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "physical_book_history")
public class PhysicalBookHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private int year;
    private String category;
    private int quantity;

    private String action;  // DELETED or UPDATED
    private LocalDateTime date;

    public PhysicalBookHistory() {}

    public PhysicalBookHistory(PhysicalBook b, String action) {
        this.title = b.getTitle();
        this.author = b.getAuthor();
        this.year = b.getYear();
        this.category = b.getCategory();
        this.quantity = b.getQuantity();
        this.action = action;
        this.date = LocalDateTime.now();
    }

    // ===============================
    // GETTERS & SETTERS – OBLIGATORII
    // ===============================

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
