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

import java.nio.file.*;
import java.util.*;

@Controller
@RequestMapping("/admin/digital")
public class AdminDigitalBookController {

    private final String BASE_PATH = "src/main/resources/static/books/";

    @Autowired
    private DigitalBookRepository bookRepo;

    @Autowired
    private DigitalBookHistoryRepository historyRepo;



    // ============================================================================================
    // LIST DIGITAL BOOKS
    // ============================================================================================
    @GetMapping
    public String listDigitalBooks(Model model) {

        List<DigitalBook> list = bookRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(DigitalBook::getTitle, String.CASE_INSENSITIVE_ORDER))
                .toList();

        Map<String, List<DigitalBook>> grouped = new LinkedHashMap<>();

        for (DigitalBook b : list) {
            String category = (b.getCategory() == null)
                    ? "other"
                    : b.getCategory().toLowerCase().trim();

            grouped.computeIfAbsent(category, c -> new ArrayList<>()).add(b);
        }

        model.addAttribute("grouped", grouped);
        return "digital_books";
    }



    // ============================================================================================
    // OPEN PDF
    // ============================================================================================
    @GetMapping("/open/{id}")
    public ResponseEntity<Resource> openPdf(@PathVariable Long id) {

        DigitalBook ebook = bookRepo.findById(id).orElse(null);
        if (ebook == null || ebook.getFileName() == null)
            return ResponseEntity.notFound().build();

        try {
            Path path = Paths.get(BASE_PATH + ebook.getCategory() + "/" + ebook.getFileName());
            Resource res = new UrlResource(path.toUri());

            if (!res.exists()) return ResponseEntity.notFound().build();

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(res);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



    // ============================================================================================
    // DOWNLOAD PDF
    // ============================================================================================
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable Long id) {

        DigitalBook ebook = bookRepo.findById(id).orElse(null);
        if (ebook == null) return ResponseEntity.notFound().build();

        try {
            Path path = Paths.get(BASE_PATH + ebook.getCategory() + "/" + ebook.getFileName());
            Resource res = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + ebook.getFileName() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(res);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }



    // ============================================================================================
    // ADD — FORM
    // ============================================================================================
    @GetMapping("/add")
    public String addForm(@RequestParam(required = false) String category, Model model) {

        DigitalBook ebook = new DigitalBook();
        if (category != null)
            ebook.setCategory(category);

        model.addAttribute("ebook", ebook);
        return "digital_upload";
    }



    // ============================================================================================
    // ADD — SAVE
    // ============================================================================================
    @PostMapping("/add")
    public String saveNewBook(@ModelAttribute DigitalBook ebook,
                              @RequestParam("file") MultipartFile file) {

        try {
            String folder = BASE_PATH + ebook.getCategory().trim();
            Files.createDirectories(Paths.get(folder));

            String cleanName = file.getOriginalFilename().replace(" ", "_");
            Path path = Paths.get(folder + "/" + cleanName);
            Files.write(path, file.getBytes());

            ebook.setFileName(cleanName);
            ebook.setFormat("Digital");
            ebook.setQuantity(1);
            ebook.setBorrowed(0);
            ebook.setAvailable(true);

            bookRepo.save(ebook);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/digital";
    }



    // ============================================================================================
    // EDIT — FORM
    // ============================================================================================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {

        DigitalBook ebook = bookRepo.findById(id).orElse(null);
        if (ebook == null) return "redirect:/admin/digital";

        model.addAttribute("ebook", ebook);
        return "digital_edit";
    }



    // ============================================================================================
    // EDIT — SAVE
    // ============================================================================================
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute DigitalBook updated,
                         @RequestParam("file") MultipartFile file) {

        DigitalBook ebook = bookRepo.findById(id).orElse(null);
        if (ebook == null) return "redirect:/admin/digital";

        try {
            ebook.setTitle(updated.getTitle());
            ebook.setAuthor(updated.getAuthor());
            ebook.setYear(updated.getYear());
            ebook.setCategory(updated.getCategory().trim());

            if (!file.isEmpty()) {
                String folder = BASE_PATH + ebook.getCategory();
                Files.createDirectories(Paths.get(folder));

                String cleanName = file.getOriginalFilename().replace(" ", "_");
                Path path = Paths.get(folder + "/" + cleanName);
                Files.write(path, file.getBytes());

                ebook.setFileName(cleanName);
            }

            bookRepo.save(ebook);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/digital";
    }



    // ============================================================================================
    // DELETE + SAVE TO HISTORY
    // ============================================================================================
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {

        DigitalBook ebook = bookRepo.findById(id).orElse(null);
        if (ebook == null) return "redirect:/admin/digital";

        historyRepo.save(new DigitalBookHistory(ebook, "DELETED"));
        bookRepo.delete(ebook);

        return "redirect:/admin/digital";
    }



    // ============================================================================================
    // HISTORY PAGE
    // ============================================================================================
    @GetMapping("/history")
    public String getHistory(Model model) {

        model.addAttribute("history", historyRepo.findAll());
        return "digital_history";
    }



    // ============================================================================================
    // RESTORE
    // ============================================================================================
    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {

        DigitalBookHistory h = historyRepo.findById(id).orElse(null);
        if (h == null) return "redirect:/admin/digital/history";

        DigitalBook restored = new DigitalBook(
                h.getTitle(),
                h.getAuthor(),
                h.getYear(),
                h.getCategory(),
                h.getFileName()
        );

        restored.setFormat("Digital");
        restored.setQuantity(1);
        restored.setBorrowed(0);
        restored.setAvailable(true);

        bookRepo.save(restored);
        historyRepo.delete(h);

        return "redirect:/admin/digital/history";
    }



    // ============================================================================================
    // DELETE SINGLE HISTORY ENTRY
    // ============================================================================================
    @GetMapping("/history/delete/{id}")
    public String deleteHistoryEntry(@PathVariable Long id) {
        historyRepo.deleteById(id);
        return "redirect:/admin/digital/history";
    }



    // ============================================================================================
    // CLEAR ALL HISTORY
    // ============================================================================================
    @GetMapping("/history/clear")
    public String clearHistory() {
        historyRepo.deleteAll();
        return "redirect:/admin/digital/history";
    }
}
