/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;
import config.DBConnection;
import config.DBConnection;
import java.sql.Connection;
/**
 *
 * @author Arbi Wiratama
 */
public class testConnection {
    public static void main(String[] args) {
        try{
            Connection conn = DBConnection.getConnection();
            if(conn != null) {
                System.out.println("Koneksi Berhasil");
            }
            else {
                System.out.println("Koneksi Gagal");
            }
        }
        catch(Exception e) {
           e.printStackTrace();
        }
    }
}
