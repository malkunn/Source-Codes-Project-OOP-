# Source-Codes-Project-OOP
BICS 1304 SECTION 3  
JavaFX **Sport Court Booking System**  

🎾 **Courtify - Sport Court Management System**  
Courtify is a centralized JavaFX-based desktop application designed to modernize sport center operations. It provides a digital solution for booking various courts including Badminton, Futsal, Tennis, Pickleball, and Ping Pong while offering robust administrative tools for facility and revenue management.

---

## 🚀 Project Overview
Many sports centers still rely on manual logs, leading to double-bookings and pricing inaccuracies. Courtify addresses these challenges by automating the booking lifecycle, from real-time availability checks to automated receipt generation.

---

## Key Features
- **Smart Booking System:** Real-time conflict detection and past-time booking prevention.  
- **Dynamic Pricing:** Automatic differentiation between Day (9 AM – 6 PM) and Night (6 PM – 2 AM) rates based on the sport type.  
- **Admin Control Panel:** Tools to manage courts (Add/Remove/Maintenance), monitor all bookings, and suspend/activate user accounts.  
- **Revenue Analytics:** Categorized financial reports showing income by sport type.  
- **Automated Receipts:** Generation of professional `.txt` receipts and refund documents.  

---

## 🏗 System Architecture
The project follows a modular Object-Oriented Programming (OOP) design to ensure maintainability and scalability.

### Core Classes & Persons In Charge (PIC)
| Class Name          | Purpose / Responsibility                                               | PIC Name                                    |
|--------------------|-----------------------------------------------------------------------|--------------------------------------------|
| **User**           | Manages user account data and authentication                           | Muhammad Zamir Fikri Bin Mohd Zamri       |
| **Court**          | Represents individual sports courts with pricing                       | Muhammad Zamir Fikri Bin Mohd Zamri       |
| **Booking**        | Manages court reservations, cancellations, and refunds                 | Muhd Akmal Hakim Bin Saifuddin            |
| **Payment**        | Processes payments and generates receipts                               | Muhd Akmal Hakim Bin Saifuddin            |
| **Schedule**       | Manages time slots for courts                                           | Muhammad Hafiz Hamzah Bin Fansuri         |
| **Admin**          | Administrator operations and management                                 | Muhammad Hafiz Hamzah Bin Fansuri         |
| **SportCentre**    | Represents the entire sports facility                                    | Abdul Hadi Bin Zawawi                      |
| **DataManager**    | Handles file operations and data persistence                             | Abdul Hadi Bin Zawawi                      |
| **BookingValidator** | Validates bookings and prevents conflicts                              | Group Collaboration                        |

---

## 🛠 Tech Stack
- **Language:** Java  
- **Framework:** JavaFX (UI/UX)  
- **Storage:** Text-based File I/O (CSV format)  
- **Time Management:** java.time API  

---

