package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.DigitalBookHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DigitalBookHistoryRepository extends JpaRepository<DigitalBookHistory, Long> {
}
