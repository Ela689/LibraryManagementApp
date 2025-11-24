package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Găsește toate cărțile după edition (PHYSICAL, BORROWABLE)
    List<Book> findByEdition(String edition);

    List<Book> findByFormat(String format);

    List<Book> findByCategory(String category);
}
