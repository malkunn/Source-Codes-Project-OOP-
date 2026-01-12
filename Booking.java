/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projectoop;

/**
 *
 * @author Abdul Hadi
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Booking {
    private String bookingID;
    private User user;
    private Court court;
    private String date;
    private String timeSlot;
    private int duration; // in hours
    private String paymentStatus;
    private LocalDateTime bookingDateTime;
    private double totalAmount;

    public Booking(String bookingID, User user, Court court, String date, String timeSlot, int duration) {
        this.bookingID = bookingID;
        this.user = user;
        this.court = court;
        this.date = date;
        this.timeSlot = timeSlot;
        this.duration = duration;
        this.paymentStatus = "UNPAID";
        this.bookingDateTime = LocalDateTime.now();
        calculateTotalAmount();
    }

    private void calculateTotalAmount() {
        int startHour = Integer.parseInt(timeSlot.split(":")[0]);
        this.totalAmount = court.calculatePrice(startHour, duration);
    }

    public void createBooking() {
        System.out.println("Booking Created Successfully!");
        System.out.println("Booking ID: " + bookingID);
    }

    public boolean canRefund() {
        try {
            String[] dateParts = date.split("-");
            String[] timeParts = timeSlot.split(":");
            LocalDateTime bookingTime = LocalDateTime.of(
                Integer.parseInt(dateParts[0]),
                Integer.parseInt(dateParts[1]),
                Integer.parseInt(dateParts[2]),
                Integer.parseInt(timeParts[0]),
                Integer.parseInt(timeParts[1])
            );
            long hoursDifference = ChronoUnit.HOURS.between(LocalDateTime.now(), bookingTime);
            return hoursDifference >= 2;
        } catch (Exception e) {
            return false;
        }
    }

    public void cancelBooking() {
        System.out.println("Booking " + bookingID + " has been cancelled.");
        if (canRefund()) {
            paymentStatus = "REFUNDED";
        } else {
            paymentStatus = "CANCELLED - NO REFUND";
        }
        court.updateStatus("Available");
    }

    public void viewBookingDetails() {
        System.out.println("===== BOOKING DETAILS =====");
        System.out.println("Booking ID : " + bookingID);
        System.out.println("User : " + user.getName());
        System.out.println("Court : " + court.getCourtName());
        System.out.println("Date : " + date);
        System.out.println("Time Slot : " + timeSlot);
        System.out.println("Duration : " + duration + " hour(s)");
        System.out.println("Total Amount : RM " + String.format("%.2f", totalAmount));
        System.out.println("Payment : " + paymentStatus);
        System.out.println("===========================");
    }

    public void setPaymentStatus(String status) {
        this.paymentStatus = status;
    }

    public String getBookingID() { return bookingID; }
    public String getPaymentStatus() { return paymentStatus; }
    public User getUser() { return user; }
    public Court getCourt() { return court; }
    public String getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }
    public int getDuration() { return duration; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getBookingDateTime() { return bookingDateTime; }
    
    public void setDuration(int duration) {
        this.duration = duration;
        calculateTotalAmount();
    }
}
