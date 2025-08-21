package Dao;

import Bean.RegisterBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterDao {

    public boolean registerUser(RegisterBean user){
        boolean status = false;
        try(Connection conn = DBconnection.getConnection()){
            // Check duplicate username/email
            String check = "SELECT * FROM users WHERE username=? OR email=?";
            PreparedStatement psCheck = conn.prepareStatement(check);
            psCheck.setString(1, user.getUsername());
            psCheck.setString(2, user.getEmail());
            ResultSet rs = psCheck.executeQuery();
            if(rs.next()) return false;

            String sql = "INSERT INTO users(username,email,password,role) VALUES(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword()); // already hashed
            ps.setString(4, user.getRole());
            status = ps.executeUpdate() > 0;
        } catch(Exception e){
            e.printStackTrace();
        }
        return status;
    }
}
