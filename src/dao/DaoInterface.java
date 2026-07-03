/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.util.ArrayList;
/**
 *
 * @author Arbi Wiratama
 */
public interface DaoInterface<T> {
    int insert(T data);
    int update(T data);
    int delete(int id);
    ArrayList<T> getAll();
    ArrayList<T> search(String keyword);
    ArrayList<T> pagination(int page,int recordsPerPage);
    int getTotalData();
}
