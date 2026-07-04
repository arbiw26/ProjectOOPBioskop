/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import model.User;
/**
 *
 * @author Arbi Wiratama
 */
public interface UserInterface {
    User findByEmail(String email);
    int insert(User user);
    int update(User user);
}
