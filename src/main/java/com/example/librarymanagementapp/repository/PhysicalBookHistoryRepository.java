package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.PhysicalBookHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysicalBookHistoryRepository extends JpaRepository<PhysicalBookHistory, Long> {
}
