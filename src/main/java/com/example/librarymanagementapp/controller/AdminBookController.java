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

import java.io.IOException;
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

    // ================================
    // LISTĂ COMPLETĂ PENTRU ADMIN
    // ================================
    @GetMapping
    public String listBooks(Model model) {

        // ======== PHYSICAL BOOKS ========
        List<Book> physical = bookRepository.findByFormat("PHYSICAL");
        Map<String, List<Book>> physicalGrouped = new LinkedHashMap<>();

        for (Book b : physical) {
            physicalGrouped.computeIfAbsent(b.getCategory(), k -> new ArrayList<>()).add(b);
        }

        // ======== BORROWABLE BOOKS ========
        List<Book> borrowable = bookRepository.findByFormat("BORROWABLE");
        Map<String, List<Book>> borrowableGrouped = new LinkedHashMap<>();

        for (Book b : borrowable) {
            borrowableGrouped.computeIfAbsent(b.getCategory(), k -> new ArrayList<>()).add(b);
        }

        // ======== DIGITAL BOOKS GROUPED ========
        List<DigitalBook> ebooks = digitalBookRepository.findAll();
        Map<String, List<DigitalBook>> digitalGrouped = new LinkedHashMap<>();

        for (DigitalBook eb : ebooks) {
            digitalGrouped.computeIfAbsent(eb.getCategory(), k -> new ArrayList<>()).add(eb);
        }

        model.addAttribute("physicalBooks", physicalGrouped);
        model.addAttribute("borrowableBooks", borrowableGrouped);
        model.addAttribute("digitalBooksGrouped", digitalGrouped);

        return "admin_books";
    }

    // ================================
    // VIEW PDF
    // ================================
    @GetMapping("/open/{id}")
    public ResponseEntity<Resource> openPdf(@PathVariable Long id) throws IOException {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);
        if (ebook == null) return ResponseEntity.notFound().build();

        Path pdfPath = Paths.get("src/main/resources/static/books/" +
                ebook.getCategory() + "/" + ebook.getFileName());

        Resource resource = new UrlResource(pdfPath.toUri());
        if (!resource.exists()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    // ================================
    // DOWNLOAD PDF
    // ================================
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable Long id) throws MalformedURLException {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);
        if (ebook == null) return ResponseEntity.notFound().build();

        Path pdfPath = Paths.get("src/main/resources/static/books/" +
                ebook.getCategory() + "/" + ebook.getFileName());

        Resource resource = new UrlResource(pdfPath.toUri());
        if (!resource.exists()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + ebook.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    // ================================
    // DELETE DIGITAL BOOK
    // ================================
    @GetMapping("/delete/{id}")
    public String deleteEbook(@PathVariable Long id) {
        digitalBookRepository.deleteById(id);
        return "redirect:/admin/books";
    }

    // ================================
    // EDIT FORM
    // ================================
    @GetMapping("/edit/{id}")
    public String editEbookForm(@PathVariable Long id, Model model) {
        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);
        model.addAttribute("ebook", ebook);
        return "edit_ebook";
    }

    // ================================
    // SAVE EDITED BOOK
    // ================================
    @PostMapping("/edit/{id}")
    public String updateEbook(@PathVariable Long id,
                              @ModelAttribute DigitalBook updated) {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);

        if (ebook != null) {
            ebook.setTitle(updated.getTitle());
            ebook.setAuthor(updated.getAuthor());
            ebook.setYear(updated.getYear());
            ebook.setCategory(updated.getCategory());
            digitalBookRepository.save(ebook);
        }

        return "redirect:/admin/books";
    }
}
