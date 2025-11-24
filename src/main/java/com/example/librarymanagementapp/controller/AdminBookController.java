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

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Map<String, List<DigitalBook>> digitalGrouped = new LinkedHashMap<>();

        for (DigitalBook eb : ebooks) {
            digitalGrouped.computeIfAbsent(eb.getCategory(), k -> new ArrayList<>()).add(eb);
        }

        model.addAttribute("digitalBooksGrouped", digitalGrouped);
        return "admin_books";
    }


    // =====================================================
    // VIEW PDF
    // =====================================================
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


    // =====================================================
    // DOWNLOAD PDF
    // =====================================================
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


    // =====================================================
    // DELETE + SAVE HISTORY
    // =====================================================
    @GetMapping("/delete/{id}")
    public String deleteEbook(@PathVariable Long id) {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);

        if (ebook != null) {
            historyRepository.save(new DigitalBookHistory(ebook, "DELETED"));
            digitalBookRepository.delete(ebook);
        }

        return "redirect:/admin/books";
    }


    // =====================================================
    // EDIT FORM
    // =====================================================
    @GetMapping("/edit/{id}")
    public String editEbookForm(@PathVariable Long id, Model model) {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);

        if (ebook == null)
            return "redirect:/admin/books";

        model.addAttribute("ebook", ebook);
        return "edit_ebook";
    }


    // =====================================================
    // SAVE EDIT RESULT
    // =====================================================
    @PostMapping("/edit/{id}")
    public String updateEbook(@PathVariable Long id,
                              @ModelAttribute DigitalBook updated) {

        DigitalBook ebook = digitalBookRepository.findById(id).orElse(null);

        if (ebook != null) {

            ebook.setTitle(updated.getTitle());
            ebook.setAuthor(updated.getAuthor());
            ebook.setYear(updated.getYear());
            ebook.setCategory(updated.getCategory());
            ebook.setFileName(updated.getFileName());

            digitalBookRepository.save(ebook);
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
                    h.getCategory(),
                    h.getFileName()
            );

            // salvăm cartea restaurată
            digitalBookRepository.save(restored);

            // ștergem intrarea din istoric
            historyRepository.delete(h);
        }

        return "redirect:/admin/books/history";
    }

    // =====================================================
    // CLEAR HISTORY (DELETE ALL)
    // =====================================================
    @GetMapping("/history/clear")
    public String clearHistory() {
        historyRepository.deleteAll();
        return "redirect:/admin/books/history";
    }

}
