package Dao;

import Bean.ItemBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {

    public List<ItemBean> getAllItems() throws Exception {
        List<ItemBean> list = new ArrayList<>();
        try(Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT * FROM items";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ItemBean i = new ItemBean();
                i.setItemId(rs.getInt("item_id"));
                i.setTitle(rs.getString("title"));
                i.setAuthor(rs.getString("author"));
                i.setCategory(rs.getString("category"));
                i.setPrice(rs.getBigDecimal("price"));
                i.setStockQuantity(rs.getInt("stock_quantity"));
                i.setDateAdded(rs.getDate("date_added"));
                list.add(i);
            }
        }
        return list;
    }

    public void addItem(ItemBean i) throws Exception {
        try(Connection conn = DBconnection.getConnection()) {
            String sql = "INSERT INTO items(title,author,category,price,stock_quantity,date_added) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, i.getTitle());
            ps.setString(2, i.getAuthor());
            ps.setString(3, i.getCategory());
            ps.setBigDecimal(4, i.getPrice());
            ps.setInt(5, i.getStockQuantity());
            ps.setDate(6, i.getDateAdded());
            ps.executeUpdate();
        }
    }

    public ItemBean getItemById(int id) throws Exception {
        ItemBean i = null;
        try(Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT * FROM items WHERE item_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                i = new ItemBean();
                i.setItemId(rs.getInt("item_id"));
                i.setTitle(rs.getString("title"));
                i.setAuthor(rs.getString("author"));
                i.setCategory(rs.getString("category"));
                i.setPrice(rs.getBigDecimal("price"));
                i.setStockQuantity(rs.getInt("stock_quantity"));
                i.setDateAdded(rs.getDate("date_added"));
            }
        }
        return i;
    }

    public void updateItem(ItemBean i) throws Exception {
        try(Connection conn = DBconnection.getConnection()) {
            String sql = "UPDATE items SET title=?,author=?,category=?,price=?,stock_quantity=?,date_added=? WHERE item_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, i.getTitle());
            ps.setString(2, i.getAuthor());
            ps.setString(3, i.getCategory());
            ps.setBigDecimal(4, i.getPrice());
            ps.setInt(5, i.getStockQuantity());
            ps.setDate(6, i.getDateAdded());
            ps.setInt(7, i.getItemId());
            ps.executeUpdate();
        }
    }

    public void deleteItem(int id) throws Exception {
        try(Connection conn = DBconnection.getConnection()) {
            String sql = "DELETE FROM items WHERE item_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
