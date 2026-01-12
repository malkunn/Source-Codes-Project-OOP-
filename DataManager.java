/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projectoop;

/**
 *
 * @author Abdul Hadi
 */
import java.io.*;
import java.util.ArrayList;

public class DataManager {
    private static final String USERS_FILE = "users.txt";
    private static final String COURTS_FILE = "courts.txt";
    private static final String BOOKINGS_FILE = "bookings.txt";

    // Save users to file
    public static void saveUsers(ArrayList<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.println(user.getUserID() + "," + user.getName() + "," + 
                             user.getEmail() + "," + user.getPassword() + "," + 
                             user.getPhone() + "," + user.isSuspended());
            }
            System.out.println("Users saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    // Load users from file
    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    User user = new User(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    if (parts.length == 6) {
                        user.setSuspended(Boolean.parseBoolean(parts[5]));
                    }
                    users.add(user);
                }
            }
            System.out.println("Users loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    // Save courts to file
    public static void saveCourts(ArrayList<Court> courts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COURTS_FILE))) {
            for (Court court : courts) {
                writer.println(court.getCourtID() + "," + court.getCourtName() + "," + 
                             court.getCourtType() + "," + court.getStatus());
            }
            System.out.println("Courts saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving courts: " + e.getMessage());
        }
    }

    // Load courts from file
    public static ArrayList<Court> loadCourts() {
        ArrayList<Court> courts = new ArrayList<>();
        File file = new File(COURTS_FILE);
        if (!file.exists()) {
            return courts;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    courts.add(new Court(parts[0], parts[1], parts[2], parts[3]));
                }
            }
            System.out.println("Courts loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading courts: " + e.getMessage());
        }
        return courts;
    }

    // Save bookings to file
    public static void saveBookings(ArrayList<Booking> bookings) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Booking booking : bookings) {
                writer.println(booking.getBookingID() + "," + 
                             booking.getUser().getUserID() + "," + 
                             booking.getCourt().getCourtID() + "," + 
                             booking.getDate() + "," + 
                             booking.getTimeSlot() + "," + 
                             booking.getDuration() + "," +
                             booking.getTotalAmount() + "," +
                             booking.getPaymentStatus());
            }
            System.out.println("Bookings saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    // Load bookings from file
    public static ArrayList<Booking> loadBookings(ArrayList<User> users, ArrayList<Court> courts) {
        ArrayList<Booking> bookings = new ArrayList<>();
        File file = new File(BOOKINGS_FILE);
        if (!file.exists()) {
            return bookings;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    
                    // Handle both old format (6 parts) and new format (8 parts)
                    if (parts.length >= 6) {
                        User user = findUserByID(users, parts[1]);
                        Court court = findCourtByID(courts, parts[2]);
                        
                        if (user != null && court != null) {
                            int duration = 1; // default duration
                            String paymentStatus = "PAID"; // default status
                            
                            // New format: ID,UserID,CourtID,Date,Time,Duration,Amount,Status
                            if (parts.length >= 8) {
                                duration = Integer.parseInt(parts[5]);
                                paymentStatus = parts[7];
                            }
                            // Old format: ID,UserID,CourtID,Date,Time,Status
                            else if (parts.length == 6) {
                                // parts[5] is the status in old format
                                paymentStatus = parts[5];
                                duration = 1;
                            }
                            
                            Booking booking = new Booking(parts[0], user, court, parts[3], parts[4], duration);
                            booking.setPaymentStatus(paymentStatus);
                            bookings.add(booking);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid booking line: " + line);
                    // Continue to next line
                }
            }
            System.out.println("Bookings loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
        return bookings;
    }

    private static User findUserByID(ArrayList<User> users, String userID) {
        for (User user : users) {
            if (user.getUserID().equals(userID)) {
                return user;
            }
        }
        return null;
    }

    private static Court findCourtByID(ArrayList<Court> courts, String courtID) {
        for (Court court : courts) {
            if (court.getCourtID().equals(courtID)) {
                return court;
            }
        }
        return null;
    }
}