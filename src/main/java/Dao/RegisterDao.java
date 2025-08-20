package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import Bean.RegisterBean;
import java.security.MessageDigest;

public class RegisterDao {

    // SHA-256 hash
    public static String sha256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(byte b: hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean registerUser(RegisterBean user) {
        boolean result = false;
        String checkSql = "SELECT * FROM users WHERE username=? OR email=?";
        String insertSql = "INSERT INTO users (username,email,password,role) VALUES (?,?,?,?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            // Check duplicate
            checkStmt.setString(1, user.getUsername());
            checkStmt.setString(2, user.getEmail());
            ResultSet rs = checkStmt.executeQuery();
            if(rs.next()) {
                System.out.println("Username or Email already exists!");
                return false; // Duplicate found
            }

            // Insert new user
            try (PreparedStatement pst = conn.prepareStatement(insertSql)) {
                String hashed = sha256(user.getPassword());
                pst.setString(1, user.getUsername());
                pst.setString(2, user.getEmail());
                pst.setString(3, hashed);
                pst.setString(4, user.getRole());

                int rows = pst.executeUpdate();
                System.out.println("Inserted rows: " + rows);
                result = rows > 0;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}