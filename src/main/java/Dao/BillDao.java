package Dao;

import Bean.*;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BillDao {

    // Transaction: create bill + items + update stock, return bill_id
    public int createBill(int customerId, List<CartItemBean> cart) throws Exception {
        if (cart == null || cart.isEmpty()) throw new IllegalArgumentException("Cart is empty");

        String insertBill    = "INSERT INTO bills (customer_id, bill_date, total_amount) VALUES (?, CURRENT_DATE, 0)";
        String insertItem    = "INSERT INTO bill_items (bill_id, item_id, quantity, price) VALUES (?,?,?,?)";
        String updateStock   = "UPDATE items SET stock_quantity = stock_quantity - ? WHERE item_id=? AND stock_quantity >= ?";
        String updateTotal   = "UPDATE bills SET total_amount=? WHERE bill_id=?";

        try (Connection con = DBconnection.getConnection()) {
            con.setAutoCommit(false);
            int billId;

            // 1) Create bill
            try (PreparedStatement ps = con.prepareStatement(insertBill, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, customerId);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("Bill ID not generated");
                    billId = keys.getInt(1);
                }
            }

            // 2) Add items + update stock
            BigDecimal total = BigDecimal.ZERO;
            try (PreparedStatement psi = con.prepareStatement(insertItem);
                 PreparedStatement pss = con.prepareStatement(updateStock)) {

                for (CartItemBean ci : cart) {
                    // stock down
                    pss.setInt(1, ci.getQuantity());
                    pss.setInt(2, ci.getItemId());
                    pss.setInt(3, ci.getQuantity());
                    int upd = pss.executeUpdate();
                    if (upd == 0) {
                        con.rollback();
                        throw new SQLException("Insufficient stock for item_id=" + ci.getItemId());
                    }

                    // bill_items
                    psi.setInt(1, billId);
                    psi.setInt(2, ci.getItemId());
                    psi.setInt(3, ci.getQuantity());
                    psi.setBigDecimal(4, ci.getUnitPrice());
                    psi.addBatch();

                    total = total.add(ci.getUnitPrice().multiply(new BigDecimal(ci.getQuantity())));
                }
                psi.executeBatch();
            }

            // 3) Update total
            try (PreparedStatement ps = con.prepareStatement(updateTotal)) {
                ps.setBigDecimal(1, total);
                ps.setInt(2, billId);
                ps.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
            return billId;
        } catch (Exception e) {
            throw e;
        }
    }

    // Get bill + customer + items
    public BillBean getBill(int billId) {
        String billSql = "SELECT b.bill_id, b.customer_id, b.bill_date, b.total_amount, " +
                "c.customer_id AS c_id, c.account_number, c.name, c.address, c.telephone " +
                "FROM bills b JOIN customers c ON b.customer_id=c.customer_id WHERE b.bill_id=?";

        String itemsSql = "SELECT bi.bill_item_id, bi.item_id, bi.quantity, bi.price, i.title " +
                "FROM bill_items bi JOIN items i ON bi.item_id=i.item_id WHERE bi.bill_id=?";

        try (Connection con = DBconnection.getConnection()) {
            BillBean bill = null;

            try (PreparedStatement ps = con.prepareStatement(billSql)) {
                ps.setInt(1, billId);
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

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
