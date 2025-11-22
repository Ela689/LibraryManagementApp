package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.Book;
import com.example.librarymanagementapp.model.DigitalBook;
import com.example.librarymanagementapp.repository.BookRepository;
import com.example.librarymanagementapp.repository.DigitalBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private DigitalBookRepository digitalBookRepository;

    // ===============================
    // LIST ALL BOOKS IN 3 SECTIONS
    // ===============================
    @GetMapping
    public String listBooks(Model model) {

        // ===========================
        // PHYSICAL
        // ===========================
        List<Book> physical = bookRepository.findByEdition("PHYSICAL");
        Map<String, List<Book>> physicalGrouped = new LinkedHashMap<>();

        for (Book b : physical) {
            physicalGrouped.computeIfAbsent(b.getCategory(), k -> new ArrayList<>()).add(b);
        }

        // ===========================
        // BORROWABLE
        // ===========================
        List<Book> borrowable = bookRepository.findByEdition("BORROWABLE");
        Map<String, List<Book>> borrowableGrouped = new LinkedHashMap<>();

        for (Book b : borrowable) {
            borrowableGrouped.computeIfAbsent(b.getCategory(), k -> new ArrayList<>()).add(b);
        }

        // ===========================
        // DIGITAL
        // ===========================
        List<DigitalBook> ebooks = digitalBookRepository.findAll();
        Map<String, List<DigitalBook>> digitalGrouped = new LinkedHashMap<>();

        for (DigitalBook e : ebooks) {
            digitalGrouped.computeIfAbsent(e.getCategory(), k -> new ArrayList<>()).add(e);
        }

        model.addAttribute("physicalBooks", physicalGrouped);
        model.addAttribute("borrowableBooks", borrowableGrouped);
        model.addAttribute("digitalBooksGrouped", digitalGrouped);

        return "admin_books";
    }


    // ===========================
    // VIEW PDF
    // ===========================
    @GetMapping("/open/{id}")
    public ResponseEntity<Resource> openPdf(@PathVariable Long id) throws MalformedURLException {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);
        if (ebook == null) return ResponseEntity.notFound().build();

        Path pdf = Paths.get("src/main/resources/static/books/" +
                ebook.getCategory() + "/" + ebook.getFileName());

        Resource res = new UrlResource(pdf.toUri());
        if (!res.exists()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(res);
    }

    // ===========================
    // DOWNLOAD PDF
    // ===========================
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable Long id) throws MalformedURLException {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);
        if (ebook == null) return ResponseEntity.notFound().build();

        Path pdf = Paths.get("src/main/resources/static/books/" +
                ebook.getCategory() + "/" + ebook.getFileName());

        Resource res = new UrlResource(pdf.toUri());
        if (!res.exists()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + ebook.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(res);
    }
}
