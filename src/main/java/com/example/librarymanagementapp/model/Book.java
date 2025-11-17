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
    private String edition;
    private int year;
    private String category;

    @Column(nullable = false)
    private String formatType; // Physical | Borrowable | Ebook

    private int quantity;
    private int borrowed; // for borrowable books

    private boolean available;

    private String filePath; // for ebooks (PDF path)

    private double price;
    private String isbn;

    // ===========================
    // 🧱 Constructors
    // ===========================

    public Book() {
    }

    public Book(String title, String author, String edition, int year,
                String category, String formatType, int quantity,
                int borrowed, boolean available, String filePath,
                double price, String isbn) {

        this.title = title;
        this.author = author;
        this.edition = edition;
        this.year = year;
        this.category = category;
        this.formatType = formatType;
        this.quantity = quantity;
        this.borrowed = borrowed;
        this.available = available;
        this.filePath = filePath;
        this.price = price;
        this.isbn = isbn;
    }

    // ===========================
    // 🧩 Getters & Setters
    // ===========================

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

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
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

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(int borrowed) {
        this.borrowed = borrowed;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    // ===========================
    // 📜 toString() for debug/log
    // ===========================
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", edition='" + edition + '\'' +
                ", year=" + year +
                ", category='" + category + '\'' +
                ", formatType='" + formatType + '\'' +
                ", quantity=" + quantity +
                ", borrowed=" + borrowed +
                ", available=" + available +
                ", filePath='" + filePath + '\'' +
                ", price=" + price +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
