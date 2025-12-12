package com.example.librarymanagementapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrowable_book_history")
public class BorrowableBookHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ----------------- DATE DE BAZĂ -----------------
    private String title;
    private String author;
    private int year;
    private String category;
    private int quantity;
    private int borrowed;

    // ----------------- NOILE CÂMPURI EXTINSE -----------------
    private String collection;
    private String cover_type;
    private String edition;
    private String format;
    private String isbn;
    private Integer pages;
    private String publisher;
    private Integer release_year;
    private String translator;

    // ----------------- META -----------------
    private String action;
    private LocalDateTime date;

    public BorrowableBookHistory() {}

    // Constructor care copiază TOT din BorrowableBook
    public BorrowableBookHistory(BorrowableBook book, String action) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.year = book.getYear();
        this.category = book.getCategory();
        this.quantity = book.getQuantity();
        this.borrowed = book.getBorrowed();

        this.collection = book.getCollection();
        this.cover_type = book.getCover_type();
        this.edition = book.getEdition();
        this.format = book.getFormat();
        this.isbn = book.getIsbn();
        this.pages = book.getPages();
        this.publisher = book.getPublisher();
        this.release_year = book.getRelease_year();
        this.translator = book.getTranslator();

        this.action = action;
        this.date = LocalDateTime.now();
    }

    // ----------------- GETTERS & SETTERS -----------------
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

    public String getCollection() { return collection; }
    public void setCollection(String collection) { this.collection = collection; }

    public String getCover_type() { return cover_type; }
    public void setCover_type(String cover_type) { this.cover_type = cover_type; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public Integer getRelease_year() { return release_year; }
    public void setRelease_year(Integer release_year) { this.release_year = release_year; }

    public String getTranslator() { return translator; }
    public void setTranslator(String translator) { this.translator = translator; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
