package Dao;

import Bean.UserBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDao {

    // Get all staff rows (tolerates case/space issues in role)
    public List<UserBean> findAllStaff() {
        List<UserBean> list = new ArrayList<>();
        String sql =
            "SELECT id, username, email, role " +
            "FROM users " +
            "WHERE LOWER(TRIM(role)) = 'staff' " +
            "ORDER BY id DESC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserBean u = new UserBean();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get one staff by id (only if role is staff, tolerant)
    public UserBean findStaffById(int id) {
        String sql =
            "SELECT id, username, email, role " +
            "FROM users " +
            "WHERE id = ? AND LOWER(TRIM(role)) = 'staff'";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserBean u = new UserBean();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setEmail(rs.getString("email"));
                    u.setRole(rs.getString("role"));
                    return u;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update staff username/email; keep role as 'staff' (tolerant where clause)
    public boolean updateStaff(int id, String username, String email)
            throws SQLIntegrityConstraintViolationException {
        String sql =
            "UPDATE users " +
            "SET username = ?, email = ?, role = 'staff' " +
            "WHERE id = ? AND LOWER(TRIM(role)) = 'staff'";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException dup) {
            throw dup;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete staff (only staff; tolerant)
    public boolean deleteStaff(int id) {
        String sql = "DELETE FROM users WHERE id = ? AND LOWER(TRIM(role)) = 'staff'";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
