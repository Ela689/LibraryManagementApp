package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.BorrowedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {

    // toate împrumuturile unui user
    List<BorrowedBook> findByUserId(Long userId);

    // un user a împrumutat deja o anumită carte și nu a returnat-o
    boolean existsByUserIdAndBookIdAndReturnedFalse(Long userId, Long bookId);

    // toate împrumuturile active (nerecuperate)
    List<BorrowedBook> findByReturnedFalse();

    // toate împrumuturile întârziate (pentru penalizări)
    List<BorrowedBook> findByReturnedFalseAndDueDateBefore(java.time.LocalDate date);
}
