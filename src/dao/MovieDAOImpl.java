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
import model.Movie;
/**
 *
 * @author Arbi Wiratama
 */
public class MovieDAOImpl implements DaoInterface<Movie>{
    private Connection connection;
    public MovieDAOImpl() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public int insert(Movie movie) {

        try {

            String sql = "INSERT INTO movies(title, genre, duration, rating) VALUES(?,?,?,?)";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setInt(3, movie.getDuration());
            stmt.setString(4, movie.getRating());

            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    @Override
    public int update(Movie movie) {

        try {

            String sql = "UPDATE movies SET title=?, genre=?, duration=?, rating=? WHERE movieID=?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setInt(3, movie.getDuration());
            stmt.setString(4, movie.getRating());
            stmt.setInt(5, movie.getMovieID());

            return stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    @Override
    public int delete(int id) {
        try {
            String sql = "DELETE FROM movies WHERE movieID=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    @Override
    public ArrayList<Movie> getAll() {

        ArrayList<Movie> movies = new ArrayList<>();

        try {

            String sql = "SELECT * FROM movies";

            PreparedStatement stmt = connection.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Movie movie = new Movie();

                movie.setMovieID(rs.getInt("movieID"));
                movie.setTitle(rs.getString("title"));
                movie.setGenre(rs.getString("genre"));
                movie.setDuration(rs.getInt("duration"));
                movie.setRating(rs.getString("rating"));

                movies.add(movie);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }
    
    @Override
    public ArrayList<Movie> search(String keyword) {

        ArrayList<Movie> movies = new ArrayList<>();

        try {

            String sql = "SELECT * FROM movies WHERE title LIKE ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Movie movie = new Movie();

                movie.setMovieID(rs.getInt("movieID"));
                movie.setTitle(rs.getString("title"));
                movie.setGenre(rs.getString("genre"));
                movie.setDuration(rs.getInt("duration"));
                movie.setRating(rs.getString("rating"));

                movies.add(movie);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }
    
    @Override
    public ArrayList<Movie> pagination(int page, int recordsPerPage) {

        ArrayList<Movie> movies = new ArrayList<>();

        try {

            int start = (page - 1) * recordsPerPage;

            String sql = "SELECT * FROM movies LIMIT ?, ?";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, start);
            stmt.setInt(2, recordsPerPage);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Movie movie = new Movie();

                movie.setMovieID(rs.getInt("movieID"));
                movie.setTitle(rs.getString("title"));
                movie.setGenre(rs.getString("genre"));
                movie.setDuration(rs.getInt("duration"));
                movie.setRating(rs.getString("rating"));

                movies.add(movie);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }
    
    @Override
    public int getTotalData() {

        try {

            String sql = "SELECT COUNT(*) FROM movies";

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
