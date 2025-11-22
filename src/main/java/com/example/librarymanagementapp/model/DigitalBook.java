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
    private String fileName; // numele PDF-ului din folderul static/books/

    public DigitalBook() {}

    public DigitalBook(String title, String author, int year, String category, String fileName) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.category = category;
        this.fileName = fileName;
    }

    // GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
