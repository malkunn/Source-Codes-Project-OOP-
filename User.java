/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projectoop;

/**
 *
 * @author Abdul Hadi
 */
public class User {
    private String userID;
    private String name;
    private String email;
    private String password;
    private String phone;
    private boolean isSuspended;

    public User(String userID, String name, String email, String password, String phone) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.isSuspended = false;
    }

    public void register() {
        System.out.println("User " + name + " has been registered.");
    }

    public boolean login(String email, String password) {
        if (isSuspended) {
            System.out.println("Account is suspended. Please contact admin.");
            return false;
        }
        
        if (this.email.equals(email) && this.password.equals(password)) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid email or password!");
            return false;
        }
    }

    public void viewBooking() {
        System.out.println(name + " is viewing their bookings.");
    }

    public void makeBooking() {
        System.out.println(name + " is making a booking.");
    }

    public String getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public boolean isSuspended() { return isSuspended; }
    
    public void setSuspended(boolean suspended) { 
        this.isSuspended = suspended; 
    }
}