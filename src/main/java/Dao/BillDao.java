package Dao;

import Bean.BillBean;
import Bean.BillItemBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDao {

    // Add Bill with items
    public int addBill(BillBean bill, List<BillItemBean> items) throws SQLException {
        int billId = 0;
        Connection conn = null;
        PreparedStatement psBill = null;
        PreparedStatement psItem = null;
        ResultSet rs = null;

        try {
            conn = DBconnection.getConnection();
            conn.setAutoCommit(false);

            // Insert into bills table
            String sqlBill = "INSERT INTO bills(customer_id, bill_date, total_amount) VALUES(?,?,?)";
            psBill = conn.prepareStatement(sqlBill, Statement.RETURN_GENERATED_KEYS);
            psBill.setInt(1, bill.getCustomerId());
            psBill.setDate(2, bill.getBillDate());
            psBill.setDouble(3, bill.getTotalAmount());
            psBill.executeUpdate();

            rs = psBill.getGeneratedKeys();
            if (rs.next()) {
                billId = rs.getInt(1);
            }

            // Insert into bill_items table
            String sqlItem = "INSERT INTO bill_items(bill_id, item_id, quantity, price, total_amount) VALUES(?,?,?,?,?)";
            psItem = conn.prepareStatement(sqlItem);
            for (BillItemBean bi : items) {
                psItem.setInt(1, billId);
                psItem.setInt(2, bi.getItemId());
                psItem.setInt(3, bi.getQuantity());
                psItem.setDouble(4, bi.getPrice());
                psItem.setDouble(5, bi.getTotalAmount());
                psItem.addBatch();
            }
            psItem.executeBatch();

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (psBill != null) psBill.close();
            if (psItem != null) psItem.close();
            if (conn != null) conn.close();
        }
        return billId;
    }

    // Get item price
    public double getItemPrice(int itemId) throws SQLException {
        double price = 0;
        String sql = "SELECT price FROM items WHERE item_id=?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) price = rs.getDouble("price");
        }
        return price;
    }

    // Get all bills
    public List<BillBean> getAllBills() throws SQLException {
        List<BillBean> list = new ArrayList<>();
        String sql = "SELECT * FROM bills ORDER BY bill_id DESC";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BillBean b = new BillBean();
                b.setBillId(rs.getInt("bill_id"));
                b.setCustomerId(rs.getInt("customer_id"));
                b.setBillDate(rs.getDate("bill_date"));
                b.setTotalAmount(rs.getDouble("total_amount"));
                list.add(b);
            }
        }
        return list;
    }

    // Get bill by ID
    public BillBean getBillById(int billId) throws SQLException {
        BillBean b = null;
        String sql = "SELECT * FROM bills WHERE bill_id=?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                b = new BillBean();
                b.setBillId(rs.getInt("bill_id"));
                b.setCustomerId(rs.getInt("customer_id"));
                b.setBillDate(rs.getDate("bill_date"));
                b.setTotalAmount(rs.getDouble("total_amount"));
            }
        }
        return b;
    }

    // Get bill items by bill ID
    public List<BillItemBean> getBillItems(int billId) throws SQLException {
        List<BillItemBean> list = new ArrayList<>();
        String sql = "SELECT * FROM bill_items WHERE bill_id=?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BillItemBean bi = new BillItemBean();
                bi.setBillItemId(rs.getInt("bill_item_id"));
                bi.setBillId(rs.getInt("bill_id"));
                bi.setItemId(rs.getInt("item_id"));
                bi.setQuantity(rs.getInt("quantity"));
                bi.setPrice(rs.getDouble("price"));
                bi.setTotalAmount(rs.getDouble("total_amount"));
                list.add(bi);
            }
        }
        return list;
    }
}
