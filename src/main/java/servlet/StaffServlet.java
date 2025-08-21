package servlet;

import Bean.UserBean;
import Dao.StaffDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet(name = "StaffServlet", urlPatterns = {"/StaffServlet"})
public class StaffServlet extends HttpServlet {

    private final StaffDao staffDao = new StaffDao();

    // --- helpers ---
    private boolean requireAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        UserBean current = (session != null) ? (UserBean) session.getAttribute("currentUser") : null;
        if (current == null || current.getRole() == null || !"admin".equals(current.getRole())) {
            response.sendRedirect("login.jsp");
            return false;
        }
        return true;
    }

    private static String safe(String s, String def) { return (s == null) ? def : s; }
    private static boolean isValidEmail(String email) {
        Pattern p = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        return email != null && p.matcher(email).matches();
    }
    private void flash(HttpServletRequest req, String key, String value) {
        req.getSession().setAttribute(key, value);
    }

    // --- GET: list / edit / delete ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireAdmin(request, response)) return;

        String action = safe(request.getParameter("action"), "list");
        try {
            switch (action) {
                case "edit": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Bean.UserBean staff = staffDao.findStaffById(id);
                    if (staff == null) {
                        flash(request, "message", "Staff not found.");
                        response.sendRedirect("StaffServlet?action=list");
                        return;
                    }
                    request.setAttribute("staff", staff); // JSP expects 'staff'
                    request.getRequestDispatcher("editStaff.jsp").forward(request, response);
                    break;
                }
                case "delete": { // list JSP uses GET delete
                    int id = Integer.parseInt(request.getParameter("id"));
                    boolean ok = staffDao.deleteStaff(id);
                    flash(request, "message", ok ? "Staff deleted." : "Delete failed or user is not staff.");
                    response.sendRedirect("StaffServlet?action=list");
                    break;
                }
                case "list":
                default: {
                    List<Bean.UserBean> staffList = staffDao.findAllStaff();
                    request.setAttribute("staffList", staffList); // ✅ FIXED name (was 'stafflist')
                    request.getRequestDispatcher("stafflist.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // --- POST: update (your form posts to StaffServlet without action) ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!requireAdmin(request, response)) return;

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String username = safe(request.getParameter("username"), "").trim();
            String email = safe(request.getParameter("email"), "").trim();

            if (username.isEmpty()) {
                flash(request, "message", "Username is required.");
                response.sendRedirect("StaffServlet?action=edit&id=" + id);
                return;
            }
            if (!isValidEmail(email)) {
                flash(request, "message", "Invalid email address.");
                response.sendRedirect("StaffServlet?action=edit&id=" + id);
                return;
            }

            boolean ok;
            try {
                ok = staffDao.updateStaff(id, username, email);
            } catch (SQLIntegrityConstraintViolationException dup) {
                flash(request, "message", "Username or email already exists.");
                response.sendRedirect("StaffServlet?action=edit&id=" + id);
                return;
            }

            flash(request, "message", ok ? "Staff updated successfully." : "Update failed or user is not staff.");
            response.sendRedirect("StaffServlet?action=list");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
