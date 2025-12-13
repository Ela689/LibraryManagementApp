package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    boolean existsByUserAndBookAndReturnedFalse(User user, BorrowableBook book);

    List<BorrowRecord> findByUser(User user);

    List<BorrowRecord> findByReturnedFalse();
}
