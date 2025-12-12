package com.example.librarymanagementapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long bookId;

    private LocalDateTime borrowedAt;

    private LocalDateTime dueDate;

    private LocalDateTime returnedAt;

    private double penalty;

    public BorrowRecord() {}

    public BorrowRecord(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
        this.borrowedAt = LocalDateTime.now();
        this.dueDate = borrowedAt.plusDays(20); // ⏳ 20 zile termen
        this.penalty = 0;
    }

    // Getters & Setters...
}
