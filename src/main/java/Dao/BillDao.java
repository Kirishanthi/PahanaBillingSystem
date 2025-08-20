package Dao;

import Bean.BillBean;
import Bean.BillItemBean;
import Bean.CustomerBean;
import Bean.ItemBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDao {

    // Add a bill with multiple items
    public int addBill(BillBean bill, List<BillItemBean> billItems) throws Exception {
        int billId = 0;
        Connection conn = null;
        PreparedStatement psBill = null;
        PreparedStatement psItem = null;

        try {
            conn = DBconnection.getConnection();
            conn.setAutoCommit(false); // transaction

            // 1. Insert into bills table
            String sqlBill = "INSERT INTO bills(customer_id, bill_date, total_amount) VALUES (?, ?, ?)";
            psBill = conn.prepareStatement(sqlBill, Statement.RETURN_GENERATED_KEYS);
            psBill.setInt(1, bill.getCustomerId());
            psBill.setDate(2, bill.getBillDate());
            psBill.setDouble(3, bill.getTotalAmount());
            psBill.executeUpdate();

            ResultSet rs = psBill.getGeneratedKeys();
            if(rs.next()){
                billId = rs.getInt(1);
            }

            // 2. Insert bill items
            String sqlItem = "INSERT INTO bill_items(bill_id, item_id, quantity, price, total_amount) VALUES (?, ?, ?, ?, ?)";
            psItem = conn.prepareStatement(sqlItem);
            for(BillItemBean bi : billItems){
                psItem.setInt(1, billId);
                psItem.setInt(2, bi.getItemId());
                psItem.setInt(3, bi.getQuantity());
                psItem.setDouble(4, bi.getPrice());
                psItem.setDouble(5, bi.getTotalAmount());
                psItem.addBatch();
            }
            psItem.executeBatch();

            conn.commit();

        } catch (Exception e) {
            if(conn != null) conn.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            if(psBill != null) psBill.close();
            if(psItem != null) psItem.close();
            if(conn != null) conn.close();
        }

        return billId;
    }

    // Get all bills
    public List<BillBean> getAllBills() throws Exception {
        List<BillBean> bills = new ArrayList<>();
        try(Connection conn = DBconnection.getConnection()){
            String sql = "SELECT * FROM bills";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                BillBean bill = new BillBean();
                bill.setBillId(rs.getInt("bill_id"));
                bill.setCustomerId(rs.getInt("customer_id"));
                bill.setBillDate(rs.getDate("bill_date"));
                bill.setTotalAmount(rs.getDouble("total_amount"));
                bills.add(bill);
            }
        }
        return bills;
    }

    // Get bill by ID
    public BillBean getBillById(int billId) throws Exception {
        BillBean bill = null;
        try(Connection conn = DBconnection.getConnection()){
            String sql = "SELECT * FROM bills WHERE bill_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                bill = new BillBean();
                bill.setBillId(rs.getInt("bill_id"));
                bill.setCustomerId(rs.getInt("customer_id"));
                bill.setBillDate(rs.getDate("bill_date"));
                bill.setTotalAmount(rs.getDouble("total_amount"));
            }
        }
        return bill;
    }

    // Get bill items by bill ID
    public List<BillItemBean> getBillItems(int billId) throws Exception {
        List<BillItemBean> items = new ArrayList<>();
        try(Connection conn = DBconnection.getConnection()){
            String sql = "SELECT bi.*, i.title AS item_name FROM bill_items bi " +
                         "JOIN items i ON bi.item_id = i.item_id WHERE bi.bill_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                BillItemBean bi = new BillItemBean();
                bi.setItemId(rs.getInt("item_id"));
               
                bi.setQuantity(rs.getInt("quantity"));
                bi.setPrice(rs.getDouble("price"));
                bi.setTotalAmount(rs.getDouble("total_amount"));
                items.add(bi);
            }
        }
        return items;
    }

    // Get item price (used during bill generation)
    public double getItemPrice(int itemId) throws Exception {
        double price = 0;
        try(Connection conn = DBconnection.getConnection()){
            String sql = "SELECT price FROM items WHERE item_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                price = rs.getDouble("price");
            }
        }
        return price;
    }

    // Get customer by ID (for printing)
    public CustomerBean getCustomerById(int customerId) throws Exception {
        CustomerBean customer = null;
        try(Connection conn = DBconnection.getConnection()){
            String sql = "SELECT * FROM customers WHERE customer_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                customer = new CustomerBean();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setAccountNumber(rs.getString("account_number"));
                customer.setName(rs.getString("name"));
                customer.setAddress(rs.getString("address"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setDateRegistered(rs.getDate("date_registered"));
            }
        }
        return customer;
    }
}
