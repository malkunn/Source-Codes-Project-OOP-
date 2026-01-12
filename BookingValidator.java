/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projectoop;

/**
 *
 * @author Abdul Hadi
 */
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class BookingValidator {
    
    /**
     * Checks if a booking conflicts with existing bookings
     * Returns true if there's a conflict, false if booking is available
     */
    public static boolean hasConflict(ArrayList<Booking> existingBookings, 
                                     Court court, String date, String startTime, int duration) {
        
        for (Booking booking : existingBookings) {
            // Skip cancelled or refunded bookings
            if (booking.getPaymentStatus().contains("CANCELLED") || 
                booking.getPaymentStatus().equals("REFUNDED")) {
                continue;
            }
            
            // Check if same court and same date
            if (booking.getCourt().getCourtID().equals(court.getCourtID()) &&
                booking.getDate().equals(date)) {
                
                // Check time overlap
                if (isTimeOverlapping(booking.getTimeSlot(), booking.getDuration(), 
                                     startTime, duration)) {
                    return true; // Conflict found!
                }
            }
        }
        
        return false; // No conflict
    }
    
    /**
     * Checks if a booking time is in the past
     * Returns true if the time has already passed
     */
    public static boolean isPastTime(String date, String startTime) {
        try {
            LocalDate bookingDate = LocalDate.parse(date);
            int hour = parseHour(startTime);
            
            LocalDateTime bookingDateTime = bookingDate.atTime(hour, 0);
            LocalDateTime now = LocalDateTime.now();
            
            // Return true if booking time is before current time
            return bookingDateTime.isBefore(now);
            
        } catch (Exception e) {
            System.out.println("Error checking past time: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Checks if two time slots overlap
     */
    private static boolean isTimeOverlapping(String existingTime, int existingDuration,
                                            String newTime, int newDuration) {
        try {
            // Parse existing booking time
            int existingStartHour = parseHour(existingTime);
            int existingEndHour = existingStartHour + existingDuration;
            
            // Parse new booking time
            int newStartHour = parseHour(newTime);
            int newEndHour = newStartHour + newDuration;
            
            // Handle midnight wraparound (24-hour format)
            // For example: 23:00 + 2 hours = 01:00 next day
            
            // Check for overlap
            // Overlap occurs if:
            // - New booking starts before existing ends AND
            // - New booking ends after existing starts
            return (newStartHour < existingEndHour && newEndHour > existingStartHour);
            
        } catch (Exception e) {
            System.out.println("Error parsing time: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Parse hour from time string (e.g., "14:00" -> 14)
     */
    private static int parseHour(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        
        // Handle 24-hour format (00:00 = midnight, 01:00 = 1 AM, etc.)
        return hour;
    }
    
    /**
     * Get list of conflicting bookings for display
     */
    public static ArrayList<Booking> getConflictingBookings(ArrayList<Booking> existingBookings,
                                                            Court court, String date, 
                                                            String startTime, int duration) {
        ArrayList<Booking> conflicts = new ArrayList<>();
        
        for (Booking booking : existingBookings) {
            // Skip cancelled bookings
            if (booking.getPaymentStatus().contains("CANCELLED") || 
                booking.getPaymentStatus().equals("REFUNDED")) {
                continue;
            }
            
            if (booking.getCourt().getCourtID().equals(court.getCourtID()) &&
                booking.getDate().equals(date)) {
                
                if (isTimeOverlapping(booking.getTimeSlot(), booking.getDuration(), 
                                     startTime, duration)) {
                    conflicts.add(booking);
                }
            }
        }
        
        return conflicts;
    }
    
    /**
     * Get available time slots for a specific court and date
     * Excludes past times if booking for today
     */
    public static ArrayList<String> getAvailableTimeSlots(ArrayList<Booking> existingBookings,
                                                          Court court, String date, int duration) {
        ArrayList<String> availableSlots = new ArrayList<>();
        
        LocalDate bookingDate = LocalDate.parse(date);
        LocalDate today = LocalDate.now();
        boolean isToday = bookingDate.isEqual(today);
        int currentHour = LocalTime.now().getHour();
        
        // Operating hours: 9 AM to 2 AM (next day)
        // Time slots: 9, 10, 11, ..., 23, 0, 1
        for (int hour = 9; hour < 24; hour++) {
            // Skip past times if booking for today
            if (isToday && hour <= currentHour) {
                continue; // Skip this hour as it's in the past
            }
            
            String timeSlot = String.format("%02d:00", hour);
            if (!hasConflict(existingBookings, court, date, timeSlot, duration)) {
                availableSlots.add(timeSlot);
            }
        }
        
        // Add midnight to 2 AM slots (only if not today, or if current time allows)
        for (int hour = 0; hour < 2; hour++) {
            // For late night slots (0-2 AM), only show if:
            // - Not today, OR
            // - Today but current time is before midnight (we're still in previous day)
            if (!isToday || currentHour >= 9) {
                String timeSlot = String.format("%02d:00", hour);
                if (!hasConflict(existingBookings, court, date, timeSlot, duration)) {
                    availableSlots.add(timeSlot);
                }
            }
        }
        
        return availableSlots;
    }
    
    /**
     * Get booking details as a string for error messages
     */
    public static String formatConflictMessage(Booking conflictingBooking) {
        return String.format("Already booked by %s from %s for %d hour(s)",
            conflictingBooking.getUser().getName(),
            conflictingBooking.getTimeSlot(),
            conflictingBooking.getDuration());
    }
    
    /**
     * Get a user-friendly message for why a time slot is not available
     */
    public static String getUnavailableReason(String date, String startTime) {
        if (isPastTime(date, startTime)) {
            return "This time has already passed";
        }
        return "This time slot is already booked";
    }
}