/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projectoop;

/**
 *
 * @author Abdul Hadi
 */
public class Court {
    private String courtID;
    private String courtName;
    private String courtType;
    private String status;
    private double priceDay; // 9am - 6pm
    private double priceNight; // 6pm - 2am

    public Court(String courtID, String courtName, String courtType, String status) {
        this.courtID = courtID;
        this.courtName = courtName;
        this.courtType = courtType;
        this.status = status;
        setPricing();
    }

    private void setPricing() {
        // Normalize the court type for comparison (remove spaces, lowercase)
        String normalizedType = courtType.toLowerCase().replace(" ", "");
        
        switch (normalizedType) {
            case "badminton":
                priceDay = 14.0;
                priceNight = 24.0;
                break;
            case "futsal":
                priceDay = 70.0;
                priceNight = 100.0;
                break;
            case "tennis":
            case "pickleball":
                priceDay = 50.0;
                priceNight = 70.0;
                break;
            case "pingpong":  // Handles both "PingPong" and "Ping Pong"
                priceDay = 10.0;
                priceNight = 14.0;
                break;
            default:
                priceDay = 20.0;
                priceNight = 30.0;
        }
    }

    public boolean checkAvailability() {
        return status.equalsIgnoreCase("Available");
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("Court " + courtName + " status updated to: " + newStatus);
    }

    public void displayCourtInfo() {
        System.out.println("Court ID: " + courtID);
        System.out.println("Name: " + courtName);
        System.out.println("Type: " + courtType);
        System.out.println("Status: " + status + "\n");
    }

    public double calculatePrice(int startHour, int duration) {
        double totalPrice = 0;
        for (int i = 0; i < duration; i++) {
            int currentHour = (startHour + i) % 24;
            if (currentHour >= 9 && currentHour < 18) {
                totalPrice += priceDay;
            } else {
                totalPrice += priceNight;
            }
        }
        return totalPrice;
    }

    public String getCourtID() { return courtID; }
    public String getCourtName() { return courtName; }
    public String getCourtType() { return courtType; }
    public String getStatus() { return status; }
    public double getPriceDay() { return priceDay; }
    public double getPriceNight() { return priceNight; }
    
    @Override
    public String toString() {
        return courtName + " (" + courtType + ") - " + status;
    }
}