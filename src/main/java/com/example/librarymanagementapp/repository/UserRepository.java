package com.example.librarymanagementapp.repository;

import com.example.librarymanagementapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);      // pentru autentificare și borrow
    User findByUsername(String username); // pentru UserController și pagina de așteptare
}
