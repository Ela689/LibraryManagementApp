package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    // ✅ Metode pentru filtrarea utilizatorilor activi/inactivi
    List<User> findByActiveTrue();
    List<User> findByActiveFalse();
}
