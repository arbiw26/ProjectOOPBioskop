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
import model.Studio;
/**
 *
 * @author Arbi Wiratama
 */
public class StudioDAOImpl implements DaoInterface<Studio>{
    private Connection connection;
    public StudioDAOImpl() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public int insert(Studio studio) {

        try {

            String sql = "INSERT INTO studios(studioName, capacity, ticketPrice) VALUES(?,?,?)";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, studio.getStudioName());
            stmt.setInt(2, studio.getCapacity());
            stmt.setDouble(3, studio.getTicketPrice());

            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    @Override
    public int update(Studio studio) {

        try {

            String sql = "UPDATE studios SET studioName=?, capacity=?, ticketPrice=? WHERE studioID=?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, studio.getStudioName());
            stmt.setInt(2, studio.getCapacity());
            stmt.setDouble(3, studio.getTicketPrice());
            stmt.setInt(4, studio.getStudioID());

            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    @Override
    public int delete(int id) {
        try {
            String sql = "DELETE FROM studios WHERE studioID=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    @Override
    public ArrayList<Studio> getAll() {

        ArrayList<Studio> studios = new ArrayList<>();

        try {

            String sql = "SELECT * FROM studios";

            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Studio studio = new Studio();

                studio.setStudioID(rs.getInt("studioID"));
                studio.setStudioName(rs.getString("studioName"));
                studio.setCapacity(rs.getInt("capacity"));
                studio.setTicketPrice(rs.getDouble("ticketPrice"));

                studios.add(studio);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return studios;
    }
    
    @Override
    public ArrayList<Studio> search(String keyword) {

        ArrayList<Studio> studios = new ArrayList<>();

        try {

            String sql = "SELECT * FROM studios WHERE studioName LIKE ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Studio studio = new Studio();

                studio.setStudioID(rs.getInt("studioID"));
                studio.setStudioName(rs.getString("studioName"));
                studio.setCapacity(rs.getInt("capacity"));
                studio.setTicketPrice(rs.getDouble("ticketPrice"));

                studios.add(studio);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return studios;
    }
    
    @Override
    public ArrayList<Studio> pagination(int page, int recordsPerPage) {

        ArrayList<Studio> studios = new ArrayList<>();

        try {

            int start = (page - 1) * recordsPerPage;

            String sql = "SELECT * FROM studios LIMIT ?, ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, start);
            stmt.setInt(2, recordsPerPage);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Studio studio = new Studio();

                studio.setStudioID(rs.getInt("studioID"));
                studio.setStudioName(rs.getString("studioName"));
                studio.setCapacity(rs.getInt("capacity"));
                studio.setTicketPrice(rs.getDouble("ticketPrice"));

                studios.add(studio);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return studios;
    }
    
    @Override
    public int getTotalData() {

        try {

            String sql = "SELECT COUNT(*) FROM studios";

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
