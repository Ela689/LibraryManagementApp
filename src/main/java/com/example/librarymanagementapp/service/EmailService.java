package com.example.librarymanagementapp.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Trimite un email simplu (text)
     */
    public void sendEmail(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    /**
     * Template special pentru notificare returnare carte
     */
    public void sendBorrowReminder(
            String to,
            String username,
            String bookTitle,
            String dueDate
    ) {

        String subject = "📚 Reminder: Return book to the library";

        String text =
                "Hello " + username + ",\n\n" +
                        "This is a friendly reminder that the book:\n\n" +
                        "📖 \"" + bookTitle + "\"\n\n" +
                        "should be returned by: " + dueDate + ".\n\n" +
                        "Please make sure to return it on time to avoid late fees.\n\n" +
                        "Thank you,\n" +
                        "Library Management Team";

        sendEmail(to, subject, text);
    }
}
