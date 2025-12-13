package com.example.librarymanagementapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling   // pentru task zilnic
public class LibraryManagementAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementAppApplication.class, args);
        System.out.println("🚀 Library Management App is running...");
    }
}
