package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.Book;
import com.example.librarymanagementapp.repository.BookRepository;
import com.example.librarymanagementapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // ✅ DISPLAY BOOKS BY TYPE
    @GetMapping
    public String viewBooks(Model model) {
        List<Book> physicalBooks = bookRepository.findByFormatType("Physical");
        List<Book> borrowableBooks = bookRepository.findByFormatType("Borrowable");
        List<Book> ebookBooks = bookRepository.findByFormatType("Ebook");

        model.addAttribute("physicalBooks", physicalBooks);
        model.addAttribute("borrowableBooks", borrowableBooks);
        model.addAttribute("ebookBooks", ebookBooks);

        return "admin_books";
    }

    // ✅ ADD BOOK FORM
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryRepository.findAll());
        return "book_form";
    }

    // ✅ HANDLE ADD BOOK
    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book) {
        bookRepository.save(book);
        return "redirect:/admin/books";
    }

    // ✅ EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            model.addAttribute("categories", categoryRepository.findAll());
            return "book_form";
        } else {
            return "redirect:/admin/books";
        }
    }

    // ✅ HANDLE EDIT SUBMIT
    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, @ModelAttribute("book") Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
        return "redirect:/admin/books";
    }

    // ✅ DELETE BOOK
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookRepository.deleteById(id);
        return "redirect:/admin/books";
    }
}
