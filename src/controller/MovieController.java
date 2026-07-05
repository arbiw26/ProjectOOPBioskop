/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import dao.DaoInterface;
import dao.MovieDAOImpl;
import java.util.ArrayList;
import model.Movie;

/**
 *
 * @author Arbi Wiratama
 */
public class MovieController {
    private DaoInterface movieDAO;

    public MovieController() {
        movieDAO = new MovieDAOImpl();
    }

    public void insert(Movie movie) throws Exception {

        if (movie.getTitle().trim().isEmpty()) {
            throw new Exception("Movie title cannot be empty!");
        }

        if (movie.getGenre().trim().isEmpty()) {
            throw new Exception("Please input genre!");
        }

        if (movie.getDuration() <= 0) {
            throw new Exception("Please insert duration!");
        }

        if (movieDAO.insert(movie) == 0) {
            throw new Exception("Failed to save movie!");
        }

    }

    public void update(Movie movie) throws Exception {

        if (movie.getMovieID() <= 0) {
            throw new Exception("Invalid movie ID!");
        }

        if (movie.getTitle().trim().isEmpty()) {
            throw new Exception("Movie title cannot be empty!");
        }

        if (movie.getGenre().trim().isEmpty()) {
            throw new Exception("Please input genre!");
        }

        if (movie.getDuration() <= 0) {
            throw new Exception("Please insert duration!");
        }

        if (movieDAO.update(movie) == 0) {
            throw new Exception("Failed to update movie!");
        }

    }

    public void delete(int movieID) throws Exception {

        if (movieID <= 0) {
            throw new Exception("Please select movie first!");
        }

        if (movieDAO.delete(movieID) == 0) {
            throw new Exception("Failed to delete movie!");
        }

    }

    public ArrayList<Movie> getAll() {
        return movieDAO.getAll();
    }

    public ArrayList<Movie> search(String keyword){
        return movieDAO.search(keyword);
    }

    public ArrayList<Movie> pagination(int page, int recordsPerPage) throws Exception {

        if(page <= 0){
            throw new Exception("Invalid page number!");
        }

        if(recordsPerPage <= 0){
            throw new Exception("Invalid number of records!");
        }

        return movieDAO.pagination(page, recordsPerPage);

    }

    public int getTotalData() {
        return movieDAO.getTotalData();
    }
    
    
}
