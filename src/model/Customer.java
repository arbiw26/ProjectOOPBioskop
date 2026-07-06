/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Arbi Wiratama
 */
public class Customer extends Person {
    private int customerID;
    private String membership;
    

    public Customer() {
    }

    public Customer(int customerID, String name, String phone, String membership) {
        super(name, phone);
        this.customerID = customerID;
        this.membership = membership;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
