package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.PhysicalBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysicalBookRepository extends JpaRepository<PhysicalBook, Long> {
}
