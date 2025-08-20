package Dao;

import Bean.CustomerBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL driver
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/pahana", "root", "password");
    }

    // Add new customer
    public int addCustomer(CustomerBean c) {
        int status = 0;
        String sql = "INSERT INTO customer (accountNumber, name, address, telephone, dateRegistered) VALUES (?,?,?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getAccountNumber());
            ps.setString(2, c.getName());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getTelephone());
            ps.setDate(5, c.getDateRegistered());
            status = ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    // Update existing customer
    public int updateCustomer(CustomerBean c) {
        int status = 0;
        String sql = "UPDATE customer SET accountNumber=?, name=?, address=?, telephone=?, dateRegistered=? WHERE customerId=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getAccountNumber());
            ps.setString(2, c.getName());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getTelephone());
            ps.setDate(5, c.getDateRegistered());
            ps.setInt(6, c.getCustomerId());
            status = ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    // Delete customer
    public int deleteCustomer(int customerId) {
        int status = 0;
        String sql = "DELETE FROM customer WHERE customerId=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            status = ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    // Get customer by ID
    public CustomerBean getCustomerById(int customerId) {
        CustomerBean c = null;
        String sql = "SELECT * FROM customer WHERE customerId=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                c = new CustomerBean();
                c.setCustomerId(rs.getInt("customerId"));
                c.setAccountNumber(rs.getString("accountNumber"));
                c.setName(rs.getString("name"));
                c.setAddress(rs.getString("address"));
                c.setTelephone(rs.getString("telephone"));
                c.setDateRegistered(rs.getDate("dateRegistered"));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    // Get all customers
    public List<CustomerBean> getAllCustomers() {
        List<CustomerBean> list = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                CustomerBean c = new CustomerBean();
                c.setCustomerId(rs.getInt("customerId"));
                c.setAccountNumber(rs.getString("accountNumber"));
                c.setName(rs.getString("name"));
                c.setAddress(rs.getString("address"));
                c.setTelephone(rs.getString("telephone"));
                c.setDateRegistered(rs.getDate("dateRegistered"));
                list.add(c);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
