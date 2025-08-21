package Dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class testdbconnection {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/pahana_edu?useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "12345";

        Connection conn = null;

        try {
            // Load MySQL JDBC Driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully!");

            // Establish connection
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database: " + conn.getCatalog());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close connection if open
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    System.out.println("Connection closed successfully.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
