/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.BookingDAOImpl;
import java.util.ArrayList;
import model.Booking;

public class BookingController {

    private BookingDAOImpl bookingDAO;

    public BookingController() {
        bookingDAO = new BookingDAOImpl();
    }

    public void insert(Booking booking) throws Exception {

        if (booking.getCustomer() == null) {
            throw new Exception("Please select a customer!");
        }

        if (booking.getMovie() == null) {
            throw new Exception("Please select a movie!");
        }

        if (booking.getStudio() == null) {
            throw new Exception("Please select a studio!");
        }

        if (booking.getBookingDate() == null) {
            throw new Exception("Please select a booking date!");
        }

        if (booking.getShowTime() == null) {
            throw new Exception("Please select show time!");
        }

        if (booking.getSeatNumber().trim().isEmpty()) {
            throw new Exception("Seat number cannot be empty!");
        }

        if (booking.getTotalPrice() <= 0) {
            throw new Exception("Invalid total price!");
        }
        
        ArrayList<String> bookedSeats = bookingDAO.getBookedSeats(
                booking.getStudio().getStudioID(),
                booking.getBookingDate(),
                booking.getShowTime()
        );

        if (bookedSeats.contains(booking.getSeatNumber())) {
            throw new Exception("Seat has already been booked!");
        }
        
        if (bookingDAO.insert(booking) == 0) {
            throw new Exception("Failed to save booking!");
        }

    }

    public void update(Booking booking) throws Exception {

        if (booking.getBookingID() <= 0) {
            throw new Exception("Please select a booking first!");
        }

        if (booking.getCustomer() == null) {
            throw new Exception("Please select a customer!");
        }

        if (booking.getMovie() == null) {
            throw new Exception("Please select a movie!");
        }

        if (booking.getStudio() == null) {
            throw new Exception("Please select a studio!");
        }

        if (booking.getBookingDate() == null) {
            throw new Exception("Please select a booking date!");
        }

        if (booking.getShowTime() == null) {
            throw new Exception("Please select show time!");
        }

        if (booking.getSeatNumber().trim().isEmpty()) {
            throw new Exception("Seat number cannot be empty!");
        }

        if (booking.getTotalPrice() <= 0) {
            throw new Exception("Invalid total price!");
        }

        if (bookingDAO.update(booking) == 0) {
            throw new Exception("Failed to update booking!");
        }

    }

    public void delete(int bookingID) throws Exception {

        if (bookingID <= 0) {
            throw new Exception("Please select a booking first!");
        }

        if (bookingDAO.delete(bookingID) == 0) {
            throw new Exception("Failed to delete booking!");
        }

    }

    public ArrayList<Booking> getAll() {
        return bookingDAO.getAll();
    }

    public ArrayList<Booking> search(String keyword) {
        return bookingDAO.search(keyword);
    }

    public ArrayList<Booking> pagination(int page, int recordsPerPage) throws Exception {

        if (page <= 0) {
            throw new Exception("Invalid page number!");
        }

        if (recordsPerPage <= 0) {
            throw new Exception("Invalid records per page!");
        }

        return bookingDAO.pagination(page, recordsPerPage);

    }

    public int getTotalData() {
        return bookingDAO.getTotalData();
    }
    
    public ArrayList<String> getBookedSeats(int studioID,
                                        java.sql.Date bookingDate,
                                        java.sql.Time showTime) {

        return bookingDAO.getBookedSeats(
                studioID,
                bookingDate,
                showTime
        );
    }
}
