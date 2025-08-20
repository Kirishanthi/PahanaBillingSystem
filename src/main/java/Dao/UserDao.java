package Dao;

import Bean.RegisterBean;
import java.sql.*;

public class UserDao {

    // Register user
    public boolean registerUser(RegisterBean user){
        boolean success = false;

        // Check if username/email exists
        if(isUserExists(user.getUsername(), user.getEmail())) return false;

        try(Connection conn = DBconnection.getConnection()){
            String sql = "INSERT INTO users(username,email,password,role) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            int rows = ps.executeUpdate();
            success = rows > 0;
        } catch(Exception e){ e.printStackTrace(); }
        return success;
    }

    // Check duplicates
    public boolean isUserExists(String username, String email){
        try(Connection conn = DBconnection.getConnection()){
            String sql = "SELECT * FROM users WHERE username=? OR email=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch(Exception e){ e.printStackTrace(); }
        return true;
    }
}
