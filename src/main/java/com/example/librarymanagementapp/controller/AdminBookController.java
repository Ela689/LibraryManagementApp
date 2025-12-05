package com.example.librarymanagementapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This controller ONLY displays the main Admin Books menu.
 * All functionality for Physical, Borrowable, Digital
 * is handled in their dedicated controllers.
 */
@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @GetMapping
    public String adminBooksHome(Model model) {
        return "admin_books";  // This is the page with the 3 big buttons
    }
}
