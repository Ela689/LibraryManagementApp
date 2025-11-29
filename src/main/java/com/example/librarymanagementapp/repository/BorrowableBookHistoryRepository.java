package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.BorrowableBookHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowableBookHistoryRepository extends JpaRepository<BorrowableBookHistory, Long> {
}
