package com.example.librarymanagementapp.service;

import com.example.librarymanagementapp.model.BorrowedBook;
import com.example.librarymanagementapp.repository.BorrowedBookRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BorrowNotificationTask {

    private final BorrowedBookRepository borrowedRepo;

    public BorrowNotificationTask(BorrowedBookRepository borrowedRepo) {
        this.borrowedRepo = borrowedRepo;
    }

    // 🔥 rulează zilnic la 09:00
    @Scheduled(cron = "0 0 9 * * *")
    public void checkBorrows() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<BorrowedBook> dueTomorrow =
                borrowedRepo.findByReturnedFalseAndDueDateBefore(tomorrow.plusDays(1));

        List<BorrowedBook> late =
                borrowedRepo.findByReturnedFalseAndDueDateBefore(LocalDate.now());

        System.out.println("📧 NOTIFICATIONS:");

        for (BorrowedBook b : dueTomorrow) {
            if (b.getDueDate().equals(tomorrow)) {
                System.out.println("⚠ Reminder: "
                        + b.getUser().getEmail()
                        + " must return "
                        + b.getBook().getTitle()
                        + " TOMORROW");
            }
        }

        for (BorrowedBook b : late) {
            System.out.println("❌ LATE: "
                    + b.getUser().getEmail()
                    + " | Book: "
                    + b.getBook().getTitle());
        }
    }

    // 🔘 pentru butonul „Run check now”
    public void manualCheck() {
        checkBorrows();
    }
}
