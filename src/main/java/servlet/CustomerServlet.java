package servlet;

import Bean.CustomerBean;
import Bean.UserBean;
import Dao.CustomerDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UserBean user = (UserBean) session.getAttribute("currentUser");
        String role = user.getRole();

        String action = request.getParameter("action");
        CustomerDao dao = new CustomerDao();

        try {
            if ("list".equals(action)) {
                List<CustomerBean> customers = dao.getAllCustomers();
                request.setAttribute("customers", customers);
                request.getRequestDispatcher("CustomerList.jsp").forward(request, response);

            } else if ("add".equals(action)) {
                request.getRequestDispatcher("addcustomer.jsp").forward(request, response);

            } else if ("edit".equals(action)) {
                if (!"admin".equals(role)) {
                    response.sendRedirect("login.jsp");
                    return;
                }
                int id = Integer.parseInt(request.getParameter("id"));
                CustomerBean c = dao.getCustomerById(id);
                request.setAttribute("customer", c);
                request.getRequestDispatcher("editcustomer.jsp").forward(request, response);

            } else if ("delete".equals(action)) {
                if (!"admin".equals(role)) {
                    response.sendRedirect("login.jsp");
                    return;
                }
                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteCustomer(id);
                session.setAttribute("message", "Customer deleted successfully!");
                response.sendRedirect(request.getContextPath() + "/CustomerServlet?action=list");

            } else {
                response.sendRedirect(request.getContextPath() + "/CustomerServlet?action=list");
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
        String role = user.getRole();

        String action = request.getParameter("action");
        CustomerDao dao = new CustomerDao();

        try {
            if ("add".equals(action)) {
                CustomerBean c = new CustomerBean();
                c.setAccountNumber(request.getParameter("accountNumber"));
                c.setName(request.getParameter("name"));
                c.setAddress(request.getParameter("address"));
                c.setTelephone(request.getParameter("telephone"));
                c.setDateRegistered(Date.valueOf(request.getParameter("dateRegistered")));
                dao.addCustomer(c);
                session.setAttribute("message", "Customer added successfully!");
                response.sendRedirect(request.getContextPath() + "/CustomerServlet?action=list");

            } else if ("edit".equals(action)) {
                if (!"admin".equals(role)) {
                    response.sendRedirect("login.jsp");
                    return;
                }
                int id = Integer.parseInt(request.getParameter("customerId"));
                CustomerBean c = new CustomerBean();
                c.setCustomerId(id);
                c.setAccountNumber(request.getParameter("accountNumber"));
                c.setName(request.getParameter("name"));
                c.setAddress(request.getParameter("address"));
                c.setTelephone(request.getParameter("telephone"));
                c.setDateRegistered(Date.valueOf(request.getParameter("dateRegistered")));
                dao.updateCustomer(c);
                session.setAttribute("message", "Customer updated successfully!");
                response.sendRedirect(request.getContextPath() + "/CustomerServlet?action=list");

            } else {
                response.sendRedirect(request.getContextPath() + "/CustomerServlet?action=list");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
