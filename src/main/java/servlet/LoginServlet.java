package servlet;

import Bean.UserBean;
import Dao.LoginDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        LoginDao dao = new LoginDao();
        UserBean user = dao.authenticate(username, password);

        if(user != null) {
            // ✅ Session creation
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user); // store user object in session
            session.setMaxInactiveInterval(30*60); // session timeout 30 mins

            // Redirect based on role
            if("admin".equals(user.getRole())) response.sendRedirect("admin_dashboard.jsp");
            else response.sendRedirect("staff_dashboard.jsp");
        } else {
            response.sendRedirect("login.jsp?login=fail");
        }
    }
}
