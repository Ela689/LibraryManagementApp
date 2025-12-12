package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    List<BorrowRecord> findByUserId(Long userId);

    boolean existsByUserIdAndBookIdAndReturnedAtIsNull(Long userId, Long bookId);
}
