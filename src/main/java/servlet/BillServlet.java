package servlet;

import Bean.*;
import Dao.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "BillServlet", urlPatterns = {"/BillServlet"})
public class BillServlet extends HttpServlet {

    private final ItemDao itemDao = new ItemDao();
    private final CustomerDao customerDao = new CustomerDao();
    private final BillDao billDao = new BillDao();

    // Admin or Staff allowed
    private boolean requireStaffOrAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        UserBean current = (session != null) ? (UserBean) session.getAttribute("currentUser") : null;
        if (current == null || current.getRole() == null) {
            response.sendRedirect("login.jsp");
            return false;
        }
        String role = current.getRole().trim().toLowerCase();
        if (!role.equals("admin") && !role.equals("staff")) {
            response.sendRedirect("login.jsp");
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private List<CartItemBean> getCart(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        List<CartItemBean> cart = (List<CartItemBean>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private void flash(HttpServletRequest req, String key, String value) {
        req.getSession().setAttribute(key, value);
    }

    // --- helper: lookup customer by account number (no DAO change needed) ---
    private CustomerBean findCustomerByAccountNumber(String accNo) {
        if (accNo == null || accNo.trim().isEmpty()) return null;
        String sql = "SELECT customer_id, account_number, name, address, telephone, date_registered " +
                     "FROM customers WHERE account_number = ?";
        try (Connection con = DBconnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accNo.trim());
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

    // -------- GET --------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireStaffOrAdmin(request, response)) return;

        String action = Optional.ofNullable(request.getParameter("action")).orElse("form");

        try {
            switch (action) {
                case "remove": {
                    int itemId = Integer.parseInt(request.getParameter("itemId"));
                    List<CartItemBean> cart = getCart(request);
                    cart.removeIf(ci -> ci.getItemId() == itemId);
                    response.sendRedirect("BillServlet?action=form");
                    break;
                }
                case "print": {
                    int billId = Integer.parseInt(request.getParameter("billId"));
                    BillBean bill = billDao.getBill(billId);
                    if (bill == null) {
                        flash(request, "error", "Bill not found.");
                        response.sendRedirect("BillServlet?action=form");
                        return;
                    }
                    request.setAttribute("bill", bill);
                    request.getRequestDispatcher("printbill.jsp").forward(request, response);
                    break;
                }
                case "form":
                default: {
                    // Use your ItemDao.getAllItems()
                    request.setAttribute("items", itemDao.getAllItems());
                    request.getRequestDispatcher("generatebill.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // -------- POST --------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireStaffOrAdmin(request, response)) return;

        String action = Optional.ofNullable(request.getParameter("action")).orElse("add");
        try {
            switch (action) {
                case "add": {
                    int itemId = Integer.parseInt(request.getParameter("itemId"));
                    int qty = Integer.parseInt(request.getParameter("quantity"));
                    if (qty <= 0) { flash(request, "error", "Quantity must be > 0"); response.sendRedirect("BillServlet?action=form"); return; }

                    // Use your ItemDao.getItemById()
                    ItemBean item = itemDao.getItemById(itemId);
                    if (item == null) { flash(request, "error", "Item not found"); response.sendRedirect("BillServlet?action=form"); return; }
                    if (item.getStockQuantity() < qty) { flash(request, "error", "Not enough stock"); response.sendRedirect("BillServlet?action=form"); return; }

                    List<CartItemBean> cart = getCart(request);
                    boolean merged = false;
                    for (CartItemBean ci : cart) {
                        if (ci.getItemId() == itemId) {
                            ci.setQuantity(ci.getQuantity() + qty);
                            merged = true; break;
                        }
                    }
                    if (!merged) {
                        cart.add(new CartItemBean(item.getItemId(), item.getTitle(), item.getPrice(), qty));
                    }
                    flash(request, "message", "Item added to bill.");
                    response.sendRedirect("BillServlet?action=form");
                    break;
                }

                case "save": {
                    // Support both customerId OR accountNumber
                    CustomerBean cust = null;

                    String customerIdStr = request.getParameter("customerId");
                    if (customerIdStr != null && !customerIdStr.trim().isEmpty()) {
                        try {
                            int cid = Integer.parseInt(customerIdStr.trim());
                            cust = customerDao.getCustomerById(cid);
                        } catch (NumberFormatException ignore) {}
                    }
                    if (cust == null) {
                        String accountNumber = request.getParameter("accountNumber");
                        cust = findCustomerByAccountNumber(accountNumber);
                    }

                    if (cust == null) {
                        flash(request, "error", "Invalid customer (ID or Account Number).");
                        response.sendRedirect("BillServlet?action=form");
                        return;
                    }

                    List<CartItemBean> cart = getCart(request);
                    if (cart.isEmpty()) {
                        flash(request, "error", "No items in bill.");
                        response.sendRedirect("BillServlet?action=form");
                        return;
                    }
                    int billId = billDao.createBill(cust.getCustomerId(), cart);

                    // clear cart
                    request.getSession().removeAttribute("cart");

                    // go to print
                    response.sendRedirect("BillServlet?action=print&billId=" + billId);
                    break;
                }

                default:
                    response.sendRedirect("BillServlet?action=form");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
