package com.example.librarymanagementapp.controller;

import com.example.librarymanagementapp.model.DigitalBook;
import com.example.librarymanagementapp.model.DigitalBookHistory;
import com.example.librarymanagementapp.repository.DigitalBookHistoryRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    @Autowired
    private DigitalBookRepository digitalBookRepository;

    @Autowired
    private DigitalBookHistoryRepository historyRepository;

    // =====================================================
    // LIST DIGITAL BOOKS
    // =====================================================
    @GetMapping
    public String listBooks(Model model) {

        List<DigitalBook> ebooks = digitalBookRepository.findAll();
        Map<String, List<DigitalBook>> grouped = new LinkedHashMap<>();

        for (DigitalBook b : ebooks) {
            String category = b.getCategory() == null ? "other" : b.getCategory();
            grouped.computeIfAbsent(category, c -> new ArrayList<>()).add(b);
        }

        model.addAttribute("digitalBooksGrouped", grouped);
        return "admin_books";
    }

    // =====================================================
    // VIEW PDF
    // =====================================================
    @GetMapping("/open/{id}")
    public ResponseEntity<Resource> openPdf(@PathVariable Long id) throws IOException {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);
        if (ebook == null || ebook.getFileName() == null) {
            return ResponseEntity.notFound().build();
        }

        String categoryFolder = ebook.getCategory().toLowerCase().trim();
        Path pdfPath = Paths.get("src/main/resources/static/books/" +
                categoryFolder + "/" + ebook.getFileName());

        Resource resource = new UrlResource(pdfPath.toUri());
        if (!resource.exists()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    // =====================================================
    // DOWNLOAD PDF
    // =====================================================
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable Long id) throws MalformedURLException {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);
        if (ebook == null || ebook.getFileName() == null) {
            return ResponseEntity.notFound().build();
        }

        String categoryFolder = ebook.getCategory().toLowerCase().trim();
        Path pdfPath = Paths.get("src/main/resources/static/books/" +
                categoryFolder + "/" + ebook.getFileName());

        Resource resource = new UrlResource(pdfPath.toUri());
        if (!resource.exists()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + ebook.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    // =====================================================
    // ADD BOOK FORM
    // =====================================================
    @GetMapping("/add")
    public String addBookForm(@RequestParam(required = false) String category, Model model) {

        DigitalBook ebook = new DigitalBook();
        if (category != null)
            ebook.setCategory(category.toLowerCase().trim());

        model.addAttribute("ebook", ebook);
        return "book_form";  // ← FORMULAR MODERN
    }

    // =====================================================
    // SAVE NEW BOOK
    // =====================================================
    @PostMapping("/add")
    public String addBook(@ModelAttribute DigitalBook ebook,
                          @RequestParam("file") MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()) {

            String categoryFolder = ebook.getCategory().toLowerCase().trim();
            String uploadDir = "src/main/resources/static/books/" + categoryFolder;

            Files.createDirectories(Paths.get(uploadDir));

            String cleanName = file.getOriginalFilename().replace(" ", "_");
            Path filePath = Paths.get(uploadDir + "/" + cleanName);
            Files.write(filePath, file.getBytes());

            ebook.setFileName(cleanName);
        }

        digitalBookRepository.save(ebook);
        return "redirect:/admin/books";
    }

    // =====================================================
    // EDIT FORM
    // =====================================================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);
        if (ebook == null) return "redirect:/admin/books";

        model.addAttribute("ebook", ebook);
        return "book_form"; // ← ACELAȘI FORMULAR MODERN
    }

    // =====================================================
    // SAVE EDIT
    // =====================================================
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable Long id,
                             @ModelAttribute DigitalBook updated,
                             @RequestParam("file") MultipartFile file) throws IOException {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);
        if (ebook == null) return "redirect:/admin/books";

        ebook.setTitle(updated.getTitle());
        ebook.setAuthor(updated.getAuthor());
        ebook.setYear(updated.getYear());
        ebook.setCategory(updated.getCategory().toLowerCase().trim());

        if (file != null && !file.isEmpty()) {

            String folder = "src/main/resources/static/books/" +
                    ebook.getCategory().toLowerCase().trim();
            Files.createDirectories(Paths.get(folder));

            String cleanName = file.getOriginalFilename().replace(" ", "_");
            Path filePath = Paths.get(folder + "/" + cleanName);
            Files.write(filePath, file.getBytes());

            ebook.setFileName(cleanName);
        }

        digitalBookRepository.save(ebook);
        return "redirect:/admin/books";
    }

    // =====================================================
    // DELETE + HISTORY
    // =====================================================
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);
        if (ebook != null) {
            historyRepository.save(new DigitalBookHistory(ebook, "DELETED"));
            digitalBookRepository.delete(ebook);
        }

        return "redirect:/admin/books";
    }

    // =====================================================
    // HISTORY PAGE
    // =====================================================
    @GetMapping("/history")
    public String history(Model model) {
        model.addAttribute("history", historyRepository.findAll());
        return "history_books";
    }

    // =====================================================
    // RESTORE BOOK
    // =====================================================
    @GetMapping("/restore/{historyId}")
    public String restoreBook(@PathVariable Long historyId) {

        DigitalBookHistory h = historyRepository.findById(historyId).orElse(null);

        if (h != null) {

            DigitalBook restored = new DigitalBook(
                    h.getTitle(),
                    h.getAuthor(),
                    h.getYear(),
                    h.getCategory().toLowerCase().trim(),
                    h.getFileName()
            );

            digitalBookRepository.save(restored);
            historyRepository.delete(h);
        }

        return "redirect:/admin/books/history";
    }

    // =====================================================
    // CLEAR HISTORY
    // =====================================================
    @GetMapping("/history/clear")
    public String clearHistory() {
        historyRepository.deleteAll();
        return "redirect:/admin/books/history";
    }
}
