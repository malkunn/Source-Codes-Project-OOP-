/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projectoop;

/**
 *
 * @author Abdul Hadi
 */
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Payment {
    private String paymentID;
    private Booking booking;
    private double amount;
    private String paymentMethod;
    private String bankName;
    private LocalDateTime paymentDateTime;

    public Payment(String paymentID, Booking booking, double amount) {
        this.paymentID = paymentID;
        this.booking = booking;
        this.amount = amount;
        this.paymentMethod = "Cash";
        this.bankName = "";
    }

    public void setPaymentMethod(String method, String bank) {
        this.paymentMethod = method;
        this.bankName = bank;
    }

    public void makePayment() {
        this.paymentDateTime = LocalDateTime.now();
        System.out.println("Payment of RM" + amount + " for Booking " + 
                         booking.getBookingID() + " is completed.");
        booking.setPaymentStatus("PAID");
        generateReceipt();
    }

    public String generateReceipt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder receipt = new StringBuilder();
        
        receipt.append("═══════════════════════════════════════\n");
        receipt.append("           COURTIFY RECEIPT\n");
        receipt.append("     Sport Court Management System\n");
        receipt.append("═══════════════════════════════════════\n\n");
        receipt.append("Receipt ID    : ").append(paymentID).append("\n");
        receipt.append("Booking ID    : ").append(booking.getBookingID()).append("\n");
        receipt.append("Date & Time   : ").append(paymentDateTime.format(formatter)).append("\n\n");
        receipt.append("───────────────────────────────────────\n");
        receipt.append("CUSTOMER INFORMATION\n");
        receipt.append("───────────────────────────────────────\n");
        receipt.append("Name          : ").append(booking.getUser().getName()).append("\n");
        receipt.append("Email         : ").append(booking.getUser().getEmail()).append("\n");
        receipt.append("Phone         : ").append(booking.getUser().getPhone()).append("\n\n");
        receipt.append("───────────────────────────────────────\n");
        receipt.append("BOOKING DETAILS\n");
        receipt.append("───────────────────────────────────────\n");
        receipt.append("Court         : ").append(booking.getCourt().getCourtName()).append("\n");
        receipt.append("Sport Type    : ").append(booking.getCourt().getCourtType()).append("\n");
        receipt.append("Date          : ").append(booking.getDate()).append("\n");
        receipt.append("Time          : ").append(booking.getTimeSlot()).append("\n");
        receipt.append("Duration      : ").append(booking.getDuration()).append(" hour(s)\n\n");
        receipt.append("───────────────────────────────────────\n");
        receipt.append("PAYMENT DETAILS\n");
        receipt.append("───────────────────────────────────────\n");
        receipt.append("Subtotal      : RM ").append(String.format("%.2f", amount)).append("\n");
        receipt.append("Payment Method: ").append(paymentMethod);
        if (!bankName.isEmpty()) {
            receipt.append(" (").append(bankName).append(")");
        }
        receipt.append("\n");
        receipt.append("───────────────────────────────────────\n");
        receipt.append("TOTAL PAID    : RM ").append(String.format("%.2f", amount)).append("\n");
        receipt.append("───────────────────────────────────────\n\n");
        receipt.append("       Thank you for choosing\n");
        receipt.append("              COURTIFY!\n");
        receipt.append("═══════════════════════════════════════\n");
        
        saveReceiptToFile(receipt.toString());
        return receipt.toString();
    }

    private void saveReceiptToFile(String receipt) {
        try {
            String filename = "receipt_" + paymentID + ".txt";
            PrintWriter writer = new PrintWriter(new FileWriter(filename));
            writer.print(receipt);
            writer.close();
            System.out.println("Receipt saved to: " + filename);
        } catch (Exception e) {
            System.out.println("Error saving receipt: " + e.getMessage());
        }
    }

    public void refundPayment() {
        System.out.println("Payment refunded for booking " + booking.getBookingID() + ".");
        generateRefundReceipt();
    }

    public String generateRefundReceipt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder receipt = new StringBuilder();
        
        receipt.append("═══════════════════════════════════════\n");
        receipt.append("         COURTIFY REFUND RECEIPT\n");
        receipt.append("     Sport Court Management System\n");
        receipt.append("═══════════════════════════════════════\n\n");
        receipt.append("Refund ID     : REF-").append(paymentID).append("\n");
        receipt.append("Original ID   : ").append(paymentID).append("\n");
        receipt.append("Booking ID    : ").append(booking.getBookingID()).append("\n");
        receipt.append("Date & Time   : ").append(LocalDateTime.now().format(formatter)).append("\n\n");
        receipt.append("───────────────────────────────────────\n");
        receipt.append("Customer      : ").append(booking.getUser().getName()).append("\n");
        receipt.append("Court         : ").append(booking.getCourt().getCourtName()).append("\n");
        receipt.append("───────────────────────────────────────\n");
        receipt.append("REFUND AMOUNT : RM ").append(String.format("%.2f", amount)).append("\n");
        receipt.append("───────────────────────────────────────\n\n");
        receipt.append("Refund will be processed within 3-5\n");
        receipt.append("working days to your original payment\n");
        receipt.append("method.\n\n");
        receipt.append("═══════════════════════════════════════\n");
        
        saveRefundToFile(receipt.toString());
        return receipt.toString();
    }

    private void saveRefundToFile(String receipt) {
        try {
            String filename = "refund_" + paymentID + ".txt";
            PrintWriter writer = new PrintWriter(new FileWriter(filename));
            writer.print(receipt);
            writer.close();
            System.out.println("Refund receipt saved to: " + filename);
        } catch (Exception e) {
            System.out.println("Error saving refund receipt: " + e.getMessage());
        }
    }

    public String getPaymentID() { return paymentID; }
    public Booking getBooking() { return booking; }
    public double getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getBankName() { return bankName; }
    public LocalDateTime getPaymentDateTime() { return paymentDateTime; }
}
