package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.Book;
import com.example.librarymanagementapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private BookRepository bookRepository;

    // ✅ Afișează toate cărțile grupate pe tipuri (Physical / Borrowable / Ebook)
    @GetMapping
    public String listBooks(Model model) {
        List<Book> books = bookRepository.findAll();

        List<Book> physicalBooks = books.stream()
                .filter(b -> "Physical".equalsIgnoreCase(b.getFormat()))
                .collect(Collectors.toList());

        List<Book> borrowableBooks = books.stream()
                .filter(b -> "Borrowable".equalsIgnoreCase(b.getFormat()))
                .collect(Collectors.toList());

        List<Book> ebookBooks = books.stream()
                .filter(b -> "Ebook".equalsIgnoreCase(b.getFormat()))
                .collect(Collectors.toList());

        model.addAttribute("physicalBooks", physicalBooks);
        model.addAttribute("borrowableBooks", borrowableBooks);
        model.addAttribute("ebookBooks", ebookBooks);

        return "admin_books";
    }

    // ✅ Formular pentru adăugare carte nouă
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "book_form";
    }

    // ✅ Salvare carte nouă sau editată
    @PostMapping("/save")
    public String saveBook(@ModelAttribute Book book,
                           @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()) {
            String uploadDir = "src/main/resources/static/books/uploads/";
            new File(uploadDir).mkdirs();

            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            book.setFilePath("/books/uploads/" + file.getOriginalFilename());
        }

        bookRepository.save(book);
        return "redirect:/admin/books";
    }

    // ✅ Editare carte existentă
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Long id, Model model) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            return "book_form";
        } else {
            return "redirect:/admin/books";
        }
    }

    // ✅ Ștergere carte
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/admin/books";
    }
}
