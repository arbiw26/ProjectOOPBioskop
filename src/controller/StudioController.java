/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import dao.StudioDAOImpl;
import java.util.ArrayList;
import model.Studio;

/**
 *
 * @author Arbi Wiratama
 */
public class StudioController {
    private StudioDAOImpl studioDAO;

    public StudioController() {
        studioDAO = new StudioDAOImpl();
    }

    public void insert(Studio studio) throws Exception {

        if (studio.getStudioName().trim().isEmpty()) {
            throw new Exception("Studio name cannot be empty!");
        }

        if (studio.getCapacity()<= 0) {
            throw new Exception("Capacity must be greater than 0");
        }

        if (studio.getTicketPrice() <= 0) {
            throw new Exception("Please insert ticket price!");
        }

        if (studioDAO.insert(studio) == 0) {
            throw new Exception("Failed to save customer!");
        }

    }

    public void update(Studio studio) throws Exception {

        if (studio.getStudioID() <= 0) {
            throw new Exception("Invalid customer ID!");
        }

        if (studio.getStudioName().trim().isEmpty()) {
            throw new Exception("Studio name cannot be empty!");
        }

        if (studio.getCapacity() <= 0) {
            throw new Exception("Capacity must be greater than 0!");
        }

        if (studio.getTicketPrice() <= 0) {
            throw new Exception("Please insert ticket price!");
        }

        if (studioDAO.update(studio) == 0) {
            throw new Exception("Failed to update customer!");
        }

    }

    public void delete(int studioID) throws Exception {

        if (studioID <= 0) {
            throw new Exception("Please select customer first!");
        }

        if (studioDAO.delete(studioID) == 0) {
            throw new Exception("Failed to delete customer!");
        }

    }

    public ArrayList<Studio> getAll() {
        return studioDAO.getAll();
    }

    public ArrayList<Studio> search(String keyword){
        return studioDAO.search(keyword);
    }

    public ArrayList<Studio> pagination(int page, int recordsPerPage) throws Exception {

        if(page <= 0){
            throw new Exception("Invalid page number!");
        }

        if(recordsPerPage <= 0){
            throw new Exception("Invalid number of records!");
        }

        return studioDAO.pagination(page, recordsPerPage);

    }

    public int getTotalData() {
        return studioDAO.getTotalData();
    }
    
    
}
