package servlet;

import Bean.RegisterBean;
import Dao.RegisterDao;
import PasswordUtil.PasswordUtil;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Password validation
        if(!isValidPassword(password)) {
            request.setAttribute("error", "Password must be at least 8 characters, include letters and numbers");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        String role = "staff"; // hardcoded staff role

        // ✅ Hash the password before storing
        String hashedPassword = PasswordUtil.hashPassword(password);

        RegisterBean user = new RegisterBean();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setRole(role);

        RegisterDao dao = new RegisterDao();
        boolean success = dao.registerUser(user);

        if(success) {
            // Redirect to login page after registration
            response.sendRedirect(request.getContextPath() + "/login.jsp?register=success");
        } else {
            request.setAttribute("error", "Username or Email already exists!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    // Password rules
    private boolean isValidPassword(String password) {
        if(password == null) return false;
        if(password.length() < 8) return false;
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        return hasLetter && hasDigit;
    }
}
