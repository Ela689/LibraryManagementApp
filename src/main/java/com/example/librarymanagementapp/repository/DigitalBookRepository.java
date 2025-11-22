package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.DigitalBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalBookRepository extends JpaRepository<DigitalBook, Long> {
}
