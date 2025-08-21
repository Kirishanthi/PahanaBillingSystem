package Dao;

import Bean.UserBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;

public class LoginDao {

    public static String sha256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(byte b: hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch(Exception e) { e.printStackTrace(); return null; }
    }

    public UserBean authenticate(String username, String password) {
        UserBean user = null;
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try(Connection conn = DBconnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, sha256(password));

            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                user = new UserBean();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
            }

        } catch(Exception e) { e.printStackTrace(); }
        return user;
    }
}
