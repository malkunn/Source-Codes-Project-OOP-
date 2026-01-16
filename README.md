# Source-Codes-Project-OOP-
BICS 1304 SECTION 3
javaFX **sport court booking system** 


🎾 Courtify - Sport Court Management System
Courtify is a centralized JavaFX-based desktop application designed to modernize sport center operations. It provides a digital solution for booking various courts including Badminton, Futsal, Tennis, Pickleball, and Ping Pong while offering robust administrative tools for facility and revenue management.


🚀 Project Overview
Many sports centers still rely on manual logs, leading to double-bookings and pricing inaccuracies. Courtify addresses these challenges by automating the booking lifecycle, from real-time availability checks to automated receipt generation.

Key Features
>Smart Booking System: Real-time conflict detection and past-time booking prevention.
>Dynamic Pricing: Automatic differentiation between Day (9 AM – 6 PM) and Night (6 PM – 2 AM) rates based on the sport type.
>Admin Control Panel: Tools to manage courts (Add/Remove/Maintenance), monitor all bookings, and suspend/activate user accounts.
>Revenue Analytics: Categorized financial reports showing income by sport type.
>Automated Receipts: Generation of professional .txt receipts and refund documents.


🏗 System Architecture
The project follows a modular Object-Oriented Programming (OOP) design to ensure maintainability and scalability.

Core Components
>User & Admin: Handles authentication, account levels, and suspension logic.
>Booking: Manages the reservation lifecycle and the 2-hour refund eligibility rule.
>Court: Defines facility attributes and the dynamic pricing engine.
>BookingValidator: A utility layer for enforcing business rules and preventing scheduling conflicts.
>DataManager: Handles file-based data persistence across users.txt, courts.txt, and bookings.txt.


🛠 Tech Stack
>Language: Java
>Framework: JavaFX (UI/UX)
>Storage: Text-based File I/O (CSV format)
>Time Management: java.time API
