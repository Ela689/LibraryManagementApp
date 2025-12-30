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

## 🎥 Demo Videos

The application functionality is demonstrated through recorded demo videos, available via **GitHub Releases**.

### 👤 User Interface Demo
Demonstrates:
- Browsing borrowable, physical, and digital books  
- Borrowing workflow  
- Viewing personal borrowing history  
- Late fee visualization  

▶️ **User Demo Video:**  
https://github.com/Ela689/LibraryManagementApp/releases/download/v1.0-demo/user-demo.mp4

---

### 🛠️ Admin Interface Demo
Demonstrates:
- Viewing all borrow records  
- Marking books as returned  
- Running late-fee checks  
- Resetting penalties  
- Sending email notifications to users  

▶️ **Admin Demo Video:**  
https://github.com/Ela689/LibraryManagementApp/releases/download/v1.0-demo/admin-demo.mp4

---

## 📧 Email Notification Feature (Admin)

Administrators can send **email reminders** to users regarding upcoming book return deadlines.

- Emails are sent manually from the admin panel  
- Recipient email is auto-filled but editable  
- Messages are delivered via **Gmail SMTP**  
- Helps prevent late returns and penalties  

---

## 🧪 Admin Actions Explained

- **Run check now**  
  Triggers late-fee calculation for all active borrowings.

- **Reset Fee**  
  Clears the late fee for a specific borrowing record.

- **Send Notification**  
  Sends a reminder email to the user about returning a borrowed book.

---

## 🚀 How to Run the Project

### 1️⃣ Clone the repository
```bash
git clone https://github.com/Ela689/library-management-system.git
