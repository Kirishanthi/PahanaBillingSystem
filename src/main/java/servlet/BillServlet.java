package servlet;

import Bean.BillBean;
import Bean.BillItemBean;
import Bean.CustomerBean;
import Bean.ItemBean;
import Bean.UserBean;
import Dao.BillDao;
import Dao.CustomerDao;
import Dao.ItemDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/BillServlet")
public class BillServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UserBean user = (UserBean) session.getAttribute("currentUser");
        String role = user.getRole(); // admin or staff

        String action = request.getParameter("action");
        BillDao billDao = new BillDao();
        CustomerDao customerDao = new CustomerDao();
        ItemDao itemDao = new ItemDao();

        try {
            if ("list".equals(action)) {
                List<BillBean> bills = billDao.getAllBills();
                request.setAttribute("bills", bills);
                request.getRequestDispatcher("billList.jsp").forward(request, response);

            } else if ("generate".equals(action)) {
                // Both admin & staff can generate bills
                List<CustomerBean> customers = customerDao.getAllCustomers();
                List<ItemBean> items = itemDao.getAllItems();
                request.setAttribute("customers", customers);
                request.setAttribute("items", items);
                request.getRequestDispatcher("generatebill.jsp").forward(request, response);

            } else if ("print".equals(action)) {
                int billId = Integer.parseInt(request.getParameter("billId"));
                BillBean bill = billDao.getBillById(billId);
                List<BillItemBean> items = billDao.getBillItems(billId);
                request.setAttribute("bill", bill);
                request.setAttribute("items", items);
                request.getRequestDispatcher("printbill.jsp").forward(request, response);

            } else {
                response.sendRedirect("BillServlet?action=list");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UserBean user = (UserBean) session.getAttribute("currentUser");
        String role = user.getRole(); // For role-based check

        String action = request.getParameter("action");
        BillDao billDao = new BillDao();

        try {
            if ("generate".equals(action)) {
                int customerId = Integer.parseInt(request.getParameter("customerId"));
                String[] itemIds = request.getParameterValues("itemId");
                String[] quantities = request.getParameterValues("quantity");

                if(itemIds == null || quantities == null || itemIds.length != quantities.length) {
                    session.setAttribute("error", "Select at least one item with quantity!");
                    response.sendRedirect("BillServlet?action=generate");
                    return;
                }

                List<BillItemBean> billItems = new ArrayList<>();
                double totalAmount = 0;

                for (int i = 0; i < itemIds.length; i++) {
                    int itemId = Integer.parseInt(itemIds[i]);
                    int qty = Integer.parseInt(quantities[i]);
                    double price = billDao.getItemPrice(itemId);
                    double itemTotal = price * qty;
                    totalAmount += itemTotal;

                    BillItemBean bi = new BillItemBean();
                    bi.setItemId(itemId);
                    bi.setQuantity(qty);
                    bi.setPrice(price);
                    bi.setTotalAmount(itemTotal);
                    billItems.add(bi);
                }

                BillBean bill = new BillBean();
                bill.setCustomerId(customerId);
                bill.setBillDate(new Date(System.currentTimeMillis()));
                bill.setTotalAmount(totalAmount);

                int billId = billDao.addBill(bill, billItems);
                session.setAttribute("message", "Bill generated successfully! Bill ID: " + billId);
                response.sendRedirect(request.getContextPath() + "/BillServlet?action=list");
            } else {
                response.sendRedirect(request.getContextPath() + "/BillServlet?action=list");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
