/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import dao.UserDAOImpl;
import dao.UserInterface;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author Arbi Wiratama
 */
public class UserController {
     private UserInterface userDAO;

    public UserController() {
        userDAO = new UserDAOImpl();
    }

    public void register(User user) throws Exception {

        if (user.getFirstName().trim().isEmpty()) {
            throw new Exception("First name cannot be empty!");
        }

        if (user.getLastName().trim().isEmpty()) {
            throw new Exception("Last name cannot be empty!");
        }

        if (user.getEmail().trim().isEmpty()) {
            throw new Exception("Email cannot be empty!");
        }

        if (user.getPassword().trim().isEmpty()) {
            throw new Exception("Password cannot be empty!");
        }

        if (user.getCountry().trim().isEmpty()) {
            throw new Exception("Country cannot be empty!");
        }

        if (userDAO.findByEmail(user.getEmail()) != null) {
            throw new Exception("Email already registered!");
        }

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        if (userDAO.insert(user) == 0) {
            throw new Exception("Failed to register account!");
        }

    }

    public User login(String email, String password) throws Exception {

        if (email.trim().isEmpty()) {
            throw new Exception("Email cannot be empty!");
        }

        if (password.trim().isEmpty()) {
            throw new Exception("Password cannot be empty!");
        }

        User user = userDAO.findByEmail(email);

        if (user == null) {
            throw new Exception("Email is not registered!");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new Exception("Incorrect password!");
        }

        return user;

    }

        public void resetPassword(String email,
                          String newPassword,
                          String confirmPassword) throws Exception {

        if (email.trim().isEmpty()) {
            throw new Exception("Email cannot be empty!");
        }

        if (newPassword.trim().isEmpty()) {
            throw new Exception("New password cannot be empty!");
        }

        if (confirmPassword.trim().isEmpty()) {
            throw new Exception("Confirm password cannot be empty!");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new Exception("Password confirmation does not match!");
        }

        User user = userDAO.findByEmail(email);

        if (user == null) {
            throw new Exception("Email is not registered!");
        }

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        user.setPassword(hashedPassword);

        if (userDAO.update(user) == 0) {
            throw new Exception("Failed to update password!");
        }
    }
}
