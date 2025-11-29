package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.BorrowableBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowableBookRepository extends JpaRepository<BorrowableBook, Long> {
}
