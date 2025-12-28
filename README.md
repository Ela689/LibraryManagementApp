# 📚 Library Management App

## 📌 Overview
**Library Management App System** is a web-based application designed to manage books, users, and borrowing activities within a library.  
The system supports both **User** and **Admin** roles, each with well-defined permissions, ensuring secure and efficient library operations.

This project was developed as part of an academic assignment and follows standard software engineering practices, including UML modeling and layered architecture.

---

## 🛠️ Technologies Used

- **Backend:** Java, Spring Boot (Java 17)
- **Frontend:** Thymeleaf, HTML5, CSS3, Bootstrap
- **Database:** MySQL
- **ORM & Persistence:** Spring Data JPA (Hibernate)
- **Build Tool:** Maven
- **Security:** Spring Security
- **Architecture:** MVC (Controller – Service – Repository)
- **Documentation:** UML Diagrams (Use Case, Class, Sequence, Component, Deployment)

---

## 👥 User Roles

### 🔹 User
- Register and log in
- View physical, borrowable, and digital books
- Borrow available books
- View personal borrowing history
- View penalties for late returns
- Download digital books
- Logout

### 🔹 Admin
- Log in securely
- Manage books (add, edit, delete)
- Manage users
- View all borrowings
- Mark books as returned
- Reset late fees
- Run manual late fee checks
- Logout

---

## 📖 Main Features

- 📚 Book management (Physical, Borrowable, Digital)
- 🔐 Authentication and authorization
- 🔄 Borrowing and returning workflow
- ⏱️ Late fee calculation and penalties
- 🗂️ Borrowing history tracking
- 🛡️ Role-based access control
- 🧩 Clean layered architecture

---

## 🗄️ Database Structure

The database is designed to ensure data integrity and clear relationships between entities.

### Main Tables:
- `users`
- `borrowable_books`
- `physical_books`
- `digital_books`
- `borrowed_books`

Relationships:
- One **User** can have multiple **BorrowedBook** records
- One **BorrowableBook** can be borrowed multiple times
- Borrowing details are stored in the `borrowed_books` table

---

## 🚀 How to Run the Project

### 1️⃣ Clone the repository
```bash
git clone https://github.com/your-username/library-management-system.git
