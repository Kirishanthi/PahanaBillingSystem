package Dao;

import Bean.CustomerBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    public int addCustomer(CustomerBean c) {
        int status = 0;
        try(Connection conn = DBconnection.getConnection()) {
            String sql = "INSERT INTO customers(account_number,name,address,telephone,date_registered) VALUES (?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getAccountNumber());
            ps.setString(2, c.getName());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getTelephone());
            ps.setDate(5, c.getDateRegistered());
            status = ps.executeUpdate();
        } catch(Exception e){ e.printStackTrace(); }
        return status;
    }

    public int updateCustomer(CustomerBean c) {
        int status = 0;
        try(Connection conn = DBconnection.getConnection()) {
            String sql = "UPDATE customers SET account_number=?, name=?, address=?, telephone=?, date_registered=? WHERE customer_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getAccountNumber());
            ps.setString(2, c.getName());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getTelephone());
            ps.setDate(5, c.getDateRegistered());
            ps.setInt(6, c.getCustomerId());
            status = ps.executeUpdate();
        } catch(Exception e){ e.printStackTrace(); }
        return status;
    }

    public int deleteCustomer(int id) {
        int status = 0;
        try(Connection conn = DBconnection.getConnection()) {
            String sql = "DELETE FROM customers WHERE customer_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            status = ps.executeUpdate();
        } catch(Exception e){ e.printStackTrace(); }
        return status;
    }

    public CustomerBean getCustomerById(int id) {
        CustomerBean c = null;
        try(Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT * FROM customers WHERE customer_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                c = new CustomerBean();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setAccountNumber(rs.getString("account_number"));
                c.setName(rs.getString("name"));
                c.setAddress(rs.getString("address"));
                c.setTelephone(rs.getString("telephone"));
                c.setDateRegistered(rs.getDate("date_registered"));
            }
        } catch(Exception e){ e.printStackTrace(); }
        return c;
    }

    public List<CustomerBean> getAllCustomers() {
        List<CustomerBean> list = new ArrayList<>();
        try(Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT * FROM customers";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                CustomerBean c = new CustomerBean();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setAccountNumber(rs.getString("account_number"));
                c.setName(rs.getString("name"));
                c.setAddress(rs.getString("address"));
                c.setTelephone(rs.getString("telephone"));
                c.setDateRegistered(rs.getDate("date_registered"));
                list.add(c);
            }
        } catch(Exception e){ e.printStackTrace(); }
        return list;
    }
}
