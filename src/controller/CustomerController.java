/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import dao.CustomerDAOImpl;
import dao.DaoInterface;
import java.util.ArrayList;
import model.Customer;

/**
 *
 * @author Arbi Wiratama
 */
public class CustomerController {
    private DaoInterface customerDAO;

    public CustomerController() {
        customerDAO = new CustomerDAOImpl();
    }

    public void insert(Customer customer) throws Exception {

        if (customer.getName().trim().isEmpty()) {
            throw new Exception("Customer name cannot be empty!");
        }

        if (customer.getPhone().trim().isEmpty()) {
            throw new Exception("Phone number cannot be empty!");
        }

        if (!customer.getPhone().matches("\\d+")) {
            throw new Exception("Phone number must contain numbers only!");
        }

        if (customer.getMembership() == null || customer.getMembership().trim().isEmpty()) {
            throw new Exception("Please select membership!");
        }

        if (customerDAO.insert(customer) == 0) {
            throw new Exception("Failed to save customer!");
        }

    }

    public void update(Customer customer) throws Exception {

        if (customer.getCustomerID() <= 0) {
            throw new Exception("Invalid customer ID!");
        }

        if (customer.getName().trim().isEmpty()) {
            throw new Exception("Customer name cannot be empty!");
        }

        if (customer.getPhone().trim().isEmpty()) {
            throw new Exception("Phone number cannot be empty!");
        }

        if (!customer.getPhone().matches("\\d+")) {
            throw new Exception("Phone number must contain numbers only!");
        }

        if (customer.getMembership() == null || customer.getMembership().trim().isEmpty()) {
            throw new Exception("Please select membership!");
        }

        if (customerDAO.update(customer) == 0) {
            throw new Exception("Failed to update customer!");
        }

    }

    public void delete(int customerID) throws Exception {

        if (customerID <= 0) {
            throw new Exception("Please select customer first!");
        }

        if (customerDAO.delete(customerID) == 0) {
            throw new Exception("Failed to delete customer!");
        }

    }

    public ArrayList<Customer> getAll() {
        return customerDAO.getAll();
    }

    public ArrayList<Customer> search(String keyword){
        return customerDAO.search(keyword);
    }

    public ArrayList<Customer> pagination(int page, int recordsPerPage) throws Exception {

        if(page <= 0){
            throw new Exception("Invalid page number!");
        }

        if(recordsPerPage <= 0){
            throw new Exception("Invalid number of records!");
        }

        return customerDAO.pagination(page, recordsPerPage);

    }

    public int getTotalData() {
        return customerDAO.getTotalData();
    }
    
    
}
