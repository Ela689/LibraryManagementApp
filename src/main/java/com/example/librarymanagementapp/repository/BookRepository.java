package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // 🔹 Filtrare după format (Physical / Borrowable / Ebook)
    List<Book> findByFormat(String format);

    // 🔹 Filtrare după categorie
    List<Book> findByCategoryIgnoreCase(String category);

    // 🔹 Căutare după titlu
    List<Book> findByTitleContainingIgnoreCase(String title);

    // 🔹 Căutare după autor
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // 🔹 Filtrare după disponibilitate
    List<Book> findByAvailableTrue();
}
