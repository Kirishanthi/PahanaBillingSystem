package servlet;

import Bean.BillBean;
import Bean.CustomerBean;
import Dao.CustomerDao;
import Dao.SelfServiceDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "CustomerPortalServlet", urlPatterns = {"/customer"})
public class CustomerPortalServlet extends HttpServlet {

    private final SelfServiceDao selfDao = new SelfServiceDao();
    private final CustomerDao customerDao = new CustomerDao();

    private boolean requireSelf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession s = request.getSession(false);
        Integer cid = (s != null) ? (Integer) s.getAttribute("selfCustomerId") : null;
        if (cid == null) {
            response.sendRedirect(request.getContextPath() + "/customer?action=form");
            return false;
        }
        return true;
    }

    private void flash(HttpServletRequest r, String k, String v) {
        r.getSession().setAttribute(k, v);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = Optional.ofNullable(request.getParameter("action")).orElse("form");

        try {
            switch (action) {
                case "logout": {
                    HttpSession s = request.getSession(false);
                    if (s != null) s.removeAttribute("selfCustomerId");
                    response.sendRedirect(request.getContextPath() + "/customer?action=form");
                    break;
                }

                case "dashboard": {
                    if (!requireSelf(request, response)) return;
                    int cid = (int) request.getSession().getAttribute("selfCustomerId");

                    // load customer + bills
                    CustomerBean customer = customerDao.getCustomerById(cid);
                    List<BillBean> bills = selfDao.listBillsByCustomer(cid);

                    request.setAttribute("customer", customer);
                    request.setAttribute("bills", bills);
                    request.getRequestDispatcher("/customer_dashboard.jsp").forward(request, response);
                    break;
                }

                case "bill": {
                    if (!requireSelf(request, response)) return;
                    int cid = (int) request.getSession().getAttribute("selfCustomerId");
                    int billId = Integer.parseInt(request.getParameter("billId"));

                    BillBean bill = selfDao.getBillForCustomer(billId, cid);
                    if (bill == null) {
                        flash(request, "error", "Bill not found or access denied.");
                        response.sendRedirect(request.getContextPath() + "/customer?action=dashboard");
                        return;
                    }
                    request.setAttribute("bill", bill);
                    request.getRequestDispatcher("/customer_printbill.jsp").forward(request, response);
                    break;
                }

                case "form":
                default: {
                    request.getRequestDispatcher("/CustomerLogin.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // Verify account number + phone → set self session
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = Optional.ofNullable(request.getParameter("action")).orElse("verify");

        try {
            if ("verify".equals(action)) {
                String acc = request.getParameter("accountNumber");
                String tel = request.getParameter("telephone");

                CustomerBean c = selfDao.getByAccountAndPhone(acc, tel);
                if (c == null) {
                    flash(request, "error", "Invalid account number or phone.");
                    response.sendRedirect(request.getContextPath() + "/customer?action=form");
                    return;
                }
                request.getSession(true).setAttribute("selfCustomerId", c.getCustomerId());
                response.sendRedirect(request.getContextPath() + "/customer?action=dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/customer?action=form");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
