package Dao;

import Bean.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelfServiceDao {

    // Verify customer (account + phone)
    public CustomerBean getByAccountAndPhone(String accNo, String tel) {
        if (accNo == null || tel == null) return null;
        String sql = "SELECT customer_id, account_number, name, address, telephone, date_registered " +
                     "FROM customers WHERE account_number=? AND telephone=?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accNo.trim());
            ps.setString(2, tel.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CustomerBean c = new CustomerBean();
                    c.setCustomerId(rs.getInt("customer_id"));
                    c.setAccountNumber(rs.getString("account_number"));
                    c.setName(rs.getString("name"));
                    c.setAddress(rs.getString("address"));
                    c.setTelephone(rs.getString("telephone"));
                    c.setDateRegistered(rs.getDate("date_registered"));
                    return c;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // Recent bills for that customer
    public List<BillBean> listBillsByCustomer(int customerId) {
        List<BillBean> list = new ArrayList<>();
        String sql = "SELECT bill_id, customer_id, bill_date, total_amount " +
                     "FROM bills WHERE customer_id=? ORDER BY bill_id DESC";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BillBean b = new BillBean();
                    b.setBillId(rs.getInt("bill_id"));
                    b.setCustomerId(rs.getInt("customer_id"));
                    b.setBillDate(rs.getDate("bill_date"));
                    b.setTotalAmount(rs.getBigDecimal("total_amount"));
                    list.add(b);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // Full bill with items (only if it belongs to that customer)
    public BillBean getBillForCustomer(int billId, int customerId) {
        String billSql = "SELECT b.bill_id, b.customer_id, b.bill_date, b.total_amount, " +
                         "c.customer_id AS c_id, c.account_number, c.name, c.address, c.telephone " +
                         "FROM bills b JOIN customers c ON b.customer_id=c.customer_id " +
                         "WHERE b.bill_id=? AND b.customer_id=?";
        String itemsSql = "SELECT bi.bill_item_id, bi.item_id, bi.quantity, bi.price, i.title " +
                          "FROM bill_items bi JOIN items i ON bi.item_id=i.item_id WHERE bi.bill_id=?";
        try (Connection con = DBconnection.getConnection()) {
            BillBean bill = null;
            try (PreparedStatement ps = con.prepareStatement(billSql)) {
                ps.setInt(1, billId);
                ps.setInt(2, customerId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        bill = new BillBean();
                        bill.setBillId(rs.getInt("bill_id"));
                        bill.setCustomerId(rs.getInt("customer_id"));
                        bill.setBillDate(rs.getDate("bill_date"));
                        bill.setTotalAmount(rs.getBigDecimal("total_amount"));

                        CustomerBean cust = new CustomerBean();
                        cust.setCustomerId(rs.getInt("c_id"));
                        cust.setAccountNumber(rs.getString("account_number"));
                        cust.setName(rs.getString("name"));
                        cust.setAddress(rs.getString("address"));
                        cust.setTelephone(rs.getString("telephone"));
                        bill.setCustomer(cust);
                    }
                }
            }
            if (bill == null) return null;

            List<BillItemBean> items = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(itemsSql)) {
                ps.setInt(1, billId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        BillItemBean bi = new BillItemBean();
                        bi.setBillItemId(rs.getInt("bill_item_id"));
                        bi.setBillId(billId);
                        bi.setItemId(rs.getInt("item_id"));
                        bi.setQuantity(rs.getInt("quantity"));
                        bi.setPrice(rs.getBigDecimal("price"));
                        bi.setTitle(rs.getString("title"));
                        items.add(bi);
                    }
                }
            }
            bill.setItems(items);
            return bill;
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}
