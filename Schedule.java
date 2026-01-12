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

public class Schedule {
    private String scheduleID;
    private Court court;
    private ArrayList<String> availableSlots;
    private ArrayList<String> bookedSlots;

    public Schedule(String scheduleID, Court court) {
        this.scheduleID = scheduleID;
        this.court = court;
        this.availableSlots = new ArrayList<>();
        this.bookedSlots = new ArrayList<>();
    }

    public void addSlot(String slot) {
        if (!availableSlots.contains(slot)) {
            availableSlots.add(slot);
            System.out.println("Slot added: " + slot);
        } else {
            System.out.println("Slot already exists.");
        }
    }

    public void removeSlot(String slot) {
        if (availableSlots.remove(slot)) {
            System.out.println("Slot removed: " + slot);
        } else {
            System.out.println("Slot not found.");
        }
    }

    public void displaySchedule() {
        System.out.println("--- Schedule for " + court.getCourtName() + " ---");
        System.out.println("Available Slots: " + availableSlots);
        System.out.println("Booked Slots: " + bookedSlots);
    }

    public String getScheduleID() { return scheduleID; }
    public Court getCourt() { return court; }
    public ArrayList<String> getAvailableSlots() { return availableSlots; }
    public ArrayList<String> getBookedSlots() { return bookedSlots; }
}