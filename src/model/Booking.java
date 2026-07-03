/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.Date;
import java.sql.Time;
/**
 *
 * @author Arbi Wiratama
 */
public class Booking {
    private int bookingID;
    private Customer customer;
    private Movie movie;
    private Studio studio;
    private Date bookingDate;
    private Time showTime;
    private String seatNumber;
    private double totalPrice;

    public Booking() {
    }

    public Booking(int bookingID, Customer customer, Movie movie, Studio studio, Date bookingDate, Time showTime, String seatNumber, double totalPrice) {
        this.bookingID = bookingID;
        this.customer = customer;
        this.movie = movie;
        this.studio = studio;
        this.bookingDate = bookingDate;
        this.showTime = showTime;
        this.seatNumber = seatNumber;
        this.totalPrice = totalPrice;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Time getShowTime() {
        return showTime;
    }

    public void setShowTime(Time showTime) {
        this.showTime = showTime;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
