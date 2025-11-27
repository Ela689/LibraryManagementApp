//DigitalBook.java
package com.example.librarymanagementapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "digital_books")
public class DigitalBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private int year;
    private String category;

    @Column(name = "file_name")
    private String fileName;

    // extra fields for compatibility
    private String format = "Digital";
    private int quantity = 1;
    private int borrowed = 0;
    private boolean available = true;

    // empty constructor
    public DigitalBook() {}

    // constructor used for restore
    public DigitalBook(String title, String author, int year, String category, String fileName) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.category = category;
        this.fileName = fileName;

        this.format = "Digital";
        this.quantity = 1;
        this.borrowed = 0;
        this.available = true;
    }

    // GETTERS & SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getBorrowed() { return borrowed; }
    public void setBorrowed(int borrowed) { this.borrowed = borrowed; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
