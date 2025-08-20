package servlet;

import Bean.CustomerBean;
import Bean.UserBean;
import Dao.CustomerDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/AddCustomerServlet")
public class AddCustomerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("currentUser") == null){
            response.sendRedirect("login.jsp");
            return;
        }

        UserBean user = (UserBean) session.getAttribute("currentUser");
        // **Admin only access**
        if(!"admin".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        CustomerDao dao = new CustomerDao();

        try {
            if("add".equals(action)){
                CustomerBean c = new CustomerBean();
                c.setAccountNumber(request.getParameter("accountNumber"));
                c.setName(request.getParameter("name"));
                c.setAddress(request.getParameter("address"));
                c.setTelephone(request.getParameter("telephone"));
                c.setDateRegistered(Date.valueOf(request.getParameter("dateRegistered")));

                int status = dao.addCustomer(c);
                session.setAttribute("message", status>0 ? "Customer added successfully!" : "Failed to add customer!");
                response.sendRedirect("addcustomer.jsp");
            } else if("update".equals(action)){
                CustomerBean c = new CustomerBean();
                c.setCustomerId(Integer.parseInt(request.getParameter("customerId")));
                c.setAccountNumber(request.getParameter("accountNumber"));
                c.setName(request.getParameter("name"));
                c.setAddress(request.getParameter("address"));
                c.setTelephone(request.getParameter("telephone"));
                c.setDateRegistered(Date.valueOf(request.getParameter("dateRegistered")));

                int status = dao.updateCustomer(c);
                session.setAttribute("message", status>0 ? "Customer updated successfully!" : "Failed to update customer!");
                response.sendRedirect("customerList.jsp");
            }
        } catch(Exception e){
            e.printStackTrace();
            session.setAttribute("message", "Error: " + e.getMessage());
            response.sendRedirect("addcustomer.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("currentUser") == null){
            response.sendRedirect("login.jsp");
            return;
        }

        UserBean user = (UserBean) session.getAttribute("currentUser");
        // Admin only access
        if(!"admin".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        CustomerDao dao = new CustomerDao();

        try {
            if("delete".equals(action)){
                int id = Integer.parseInt(request.getParameter("id"));
                int status = dao.deleteCustomer(id);
                session.setAttribute("message", status>0 ? "Customer deleted successfully!" : "Failed to delete customer!");
                response.sendRedirect("customerList.jsp");
            } else if("edit".equals(action)){
                int id = Integer.parseInt(request.getParameter("id"));
                CustomerBean c = dao.getCustomerById(id);
                request.setAttribute("customer", c);
                RequestDispatcher rd = request.getRequestDispatcher("editCustomer.jsp");
                rd.forward(request, response);
            } else if("list".equals(action)){
                List<CustomerBean> list = dao.getAllCustomers();
                request.setAttribute("customers", list);
                RequestDispatcher rd = request.getRequestDispatcher("customerList.jsp");
                rd.forward(request, response);
            }
        } catch(Exception e){
            e.printStackTrace();
            session.setAttribute("message", "Error: " + e.getMessage());
            response.sendRedirect("customerList.jsp");
        }
    }
}
