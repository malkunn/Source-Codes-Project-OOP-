/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projectoop;

/**
 *
 * @author Abdul Hadi
 */
public class Admin {
    private String adminID;
    private String name;
    private String email;
    private String password;

    public Admin(String adminID, String name, String email, String password) {
        this.adminID = adminID;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void addCourt(SportCentre centre, Court court) {
        centre.addCourt(court);
        System.out.println(name + " added court: " + court.getCourtName());
    }

    public void removeCourt(SportCentre centre, Court court) {
        centre.removeCourt(court);
        System.out.println("Admin removed court: " + court.getCourtName());
    }

    public void viewAllBookings() {
        System.out.println("Admin viewing all bookings...");
    }

    public void manageUsers() {
        System.out.println("Admin managing users...");
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public String getAdminID() { return adminID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}