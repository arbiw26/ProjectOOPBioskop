/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Booking;
import model.Customer;
import model.Movie;
import model.Studio;
/**
 *
 * @author Made Desta Pramana
 */
public class BookingDAOImpl implements DaoInterface<Booking>{

    private Connection connection;

    public BookingDAOImpl() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private Booking mapBooking(ResultSet rs) throws SQLException {

        Customer customer = new Customer();
        customer.setCustomerID(rs.getInt("customerID"));
        customer.setName(rs.getString("customerName"));

        Movie movie = new Movie();
        movie.setMovieID(rs.getInt("movieID"));
        movie.setTitle(rs.getString("title"));

        Studio studio = new Studio();
        studio.setStudioID(rs.getInt("studioID"));
        studio.setStudioName(rs.getString("studioName"));

        Booking booking = new Booking();

        booking.setBookingID(rs.getInt("bookingID"));
        booking.setCustomer(customer);
        booking.setMovie(movie);
        booking.setStudio(studio);
        booking.setBookingDate(rs.getDate("bookingDate"));
        booking.setShowTime(rs.getTime("showTime"));
        booking.setSeatNumber(rs.getString("seatNumber"));
        booking.setTotalPrice(rs.getDouble("totalPrice"));

        return booking;
    }
    
    @Override
    public int insert(Booking booking) {

        try {

            String sql = "INSERT INTO bookings(customerID,movieID,studioID,bookingDate,showTime,seatNumber,totalPrice) VALUES(?,?,?,?,?,?,?)";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, booking.getCustomer().getCustomerID());
            stmt.setInt(2, booking.getMovie().getMovieID());
            stmt.setInt(3, booking.getStudio().getStudioID());
            stmt.setDate(4, booking.getBookingDate());
            stmt.setTime(5, booking.getShowTime());
            stmt.setString(6, booking.getSeatNumber());
            stmt.setDouble(7, booking.getTotalPrice());

            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    @Override
    public int update(Booking booking) {

        try {

            String sql = "UPDATE bookings SET customerID=?,movieID=?,studioID=?,bookingDate=?,showTime=?,seatNumber=?,totalPrice=? WHERE bookingID=?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, booking.getCustomer().getCustomerID());
            stmt.setInt(2, booking.getMovie().getMovieID());
            stmt.setInt(3, booking.getStudio().getStudioID());
            stmt.setDate(4, booking.getBookingDate());
            stmt.setTime(5, booking.getShowTime());
            stmt.setString(6, booking.getSeatNumber());
            stmt.setDouble(7, booking.getTotalPrice());
            stmt.setInt(8, booking.getBookingID());

            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    @Override
    public int delete(int id) {

        try {

            String sql = "DELETE FROM bookings WHERE bookingID=?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, id);

            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    @Override
    public ArrayList<Booking> getAll() {

        ArrayList<Booking> bookings = new ArrayList<>();

        try {

            String sql = "SELECT "
                    + "b.bookingID, "
                    + "b.bookingDate, "
                    + "b.showTime, "
                    + "b.seatNumber, "
                    + "b.totalPrice, "
                    + "c.customerID, "
                    + "c.customerName, "
                    + "m.movieID, "
                    + "m.title, "
                    + "s.studioID, "
                    + "s.studioName "
                    + "FROM bookings b "
                    + "JOIN customers c ON b.customerID = c.customerID "
                    + "JOIN movies m ON b.movieID = m.movieID "
                    + "JOIN studios s ON b.studioID = s.studioID "
                    + "ORDER BY b.bookingID DESC";

            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapBooking(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }
    
    @Override
    public ArrayList<Booking> search(String keyword) {

        ArrayList<Booking> bookings = new ArrayList<>();

        try {

            String sql = "SELECT "
                    + "b.bookingID, "
                    + "b.bookingDate, "
                    + "b.showTime, "
                    + "b.seatNumber, "
                    + "b.totalPrice, "
                    + "c.customerID, "
                    + "c.customerName, "
                    + "m.movieID, "
                    + "m.title, "
                    + "s.studioID, "
                    + "s.studioName "
                    + "FROM bookings b "
                    + "JOIN customers c ON b.customerID = c.customerID "
                    + "JOIN movies m ON b.movieID = m.movieID "
                    + "JOIN studios s ON b.studioID = s.studioID "
                    + "WHERE c.customerName LIKE ? "
                    + "OR m.title LIKE ? "
                    + "OR s.studioName LIKE ? "
                    + "OR b.seatNumber LIKE ? "
                    + "ORDER BY b.bookingID DESC";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            stmt.setString(4, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapBooking(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }
    
    @Override
    public ArrayList<Booking> pagination(int page, int recordsPerPage) {

        ArrayList<Booking> bookings = new ArrayList<>();

        try {

            int start = (page - 1) * recordsPerPage;

            String sql = "SELECT "
                    + "b.bookingID, "
                    + "b.bookingDate, "
                    + "b.showTime, "
                    + "b.seatNumber, "
                    + "b.totalPrice, "
                    + "c.customerID, "
                    + "c.customerName, "
                    + "m.movieID, "
                    + "m.title, "
                    + "s.studioID, "
                    + "s.studioName "
                    + "FROM bookings b "
                    + "JOIN customers c ON b.customerID = c.customerID "
                    + "JOIN movies m ON b.movieID = m.movieID "
                    + "JOIN studios s ON b.studioID = s.studioID "
                    + "ORDER BY b.bookingID DESC "
                    + "LIMIT ?, ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, start);
            stmt.setInt(2, recordsPerPage);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapBooking(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookings;
    }
    
    @Override
    public int getTotalData() {

        try {

            String sql = "SELECT COUNT(*) FROM bookings";

            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public ArrayList<String> getBookedSeats(int studioID,
                                        java.sql.Date bookingDate,
                                        java.sql.Time showTime) {

        ArrayList<String> bookedSeats = new ArrayList<>();
        try {
            String sql = """
                SELECT seatNumber
                FROM bookings
                WHERE studioID = ?
                AND bookingDate = ?
                AND showTime = ?
            """;
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, studioID);
            stmt.setDate(2, bookingDate);
            stmt.setTime(3, showTime);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookedSeats.add(rs.getString("seatNumber"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookedSeats;
    }
}
