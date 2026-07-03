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
import model.Customer;
/**
 *
 * @author Arbi Wiratama
 */
public class CustomerDAOImpl implements DaoInterface<Customer>{
    private Connection connection;
    public CustomerDAOImpl() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public int insert(Customer customer) {

        try {

            String sql = "INSERT INTO customers(customerName, phone, membership) VALUES(?,?,?)";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getMembership());

            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    @Override
    public int update(Customer customer) {

        try {

            String sql = "UPDATE customers SET customerName=?, phone=?, membership=? WHERE customerID=?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getMembership());
            stmt.setInt(4, customer.getCustomerID());

            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    @Override
    public int delete(int id) {
        try {
            String sql = "DELETE FROM customers WHERE customerID=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    @Override
    public ArrayList<Customer> getAll() {

        ArrayList<Customer> customers = new ArrayList<>();

        try {

            String sql = "SELECT * FROM customers";

            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Customer customer = new Customer();

                customer.setCustomerID(rs.getInt("customerID"));
                customer.setName(rs.getString("customerName"));
                customer.setPhone(rs.getString("phone"));
                customer.setMembership(rs.getString("membership"));

                customers.add(customer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }
    
    @Override
    public ArrayList<Customer> search(String keyword) {

        ArrayList<Customer> customers = new ArrayList<>();

        try {

            String sql = "SELECT * FROM customers WHERE customerName LIKE ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Customer customer = new Customer();

                customer.setCustomerID(rs.getInt("customerID"));
                customer.setName(rs.getString("customerName"));
                customer.setPhone(rs.getString("phone"));
                customer.setMembership(rs.getString("membership"));

                customers.add(customer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }
    
    @Override
    public ArrayList<Customer> pagination(int page, int recordsPerPage) {

        ArrayList<Customer> customers = new ArrayList<>();

        try {

            int start = (page - 1) * recordsPerPage;

            String sql = "SELECT * FROM customers LIMIT ?, ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, start);
            stmt.setInt(2, recordsPerPage);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Customer customer = new Customer();

                customer.setCustomerID(rs.getInt("customerID"));
                customer.setName(rs.getString("customerName"));
                customer.setPhone(rs.getString("phone"));
                customer.setMembership(rs.getString("membership"));

                customers.add(customer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customers;
    }
    
    @Override
    public int getTotalData() {

        try {

            String sql = "SELECT COUNT(*) FROM customers";

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
}
