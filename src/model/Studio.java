/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Arbi Wiratama
 */
public class Studio {
    private int studioID;
    private String studioName;
    private int capacity;
    private double ticketPrice;

    public Studio() {
    }

    public Studio(int studioID, String studioName, int capacity, double ticketPrice) {
        this.studioID = studioID;
        this.studioName = studioName;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
    }

    public int getStudioID() {
        return studioID;
    }

    public void setStudioID(int studioID) {
        this.studioID = studioID;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
    
    @Override
    public String toString() {
        return studioName;
    }
}
