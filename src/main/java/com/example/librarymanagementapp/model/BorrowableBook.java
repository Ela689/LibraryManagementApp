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
    private Integer year;
    private String category;
    private Integer quantity;

    @Column(nullable = false)
    private Integer borrowed = 0;

    // METADATA EXTINSĂ
    private String collection;
    private String cover_type;
    private String edition;
    private String format;
    private String isbn;
    private Integer pages;
    private String publisher;
    private Integer release_year;
    private String translator;

    // ================= GETTERS & SETTERS =================

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

    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getBorrowed() {
        return borrowed;
    }
    public void setBorrowed(Integer borrowed) {
        this.borrowed = borrowed;
    }

    public String getCollection() {
        return collection;
    }
    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getCover_type() {
        return cover_type;
    }
    public void setCover_type(String cover_type) {
        this.cover_type = cover_type;
    }

    public String getEdition() {
        return edition;
    }
    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPages() {
        return pages;
    }
    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getRelease_year() {
        return release_year;
    }
    public void setRelease_year(Integer release_year) {
        this.release_year = release_year;
    }

    public String getTranslator() {
        return translator;
    }
    public void setTranslator(String translator) {
        this.translator = translator;
    }
}
