package servlet;

import Bean.ItemBean;
import Bean.UserBean;
import Dao.ItemDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/ItemServlet")
public class ItemServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session==null || session.getAttribute("currentUser")==null){
            response.sendRedirect("login.jsp");
            return;
        }
        UserBean user = (UserBean) session.getAttribute("currentUser");
        String role = user.getRole();

        String action = request.getParameter("action");
        ItemDao dao = new ItemDao();

        try{
            if("list".equals(action)){
                List<ItemBean> items = dao.getAllItems();
                request.setAttribute("items", items);
                request.getRequestDispatcher("itemList.jsp").forward(request,response);

            } else if("add".equals(action)){
                request.getRequestDispatcher("additem.jsp").forward(request,response);

            } else if("edit".equals(action)){
                if(!"admin".equals(role)){
                    response.sendRedirect("ItemServlet?actiossn=list");
                    return;
                }
                int id = Integer.parseInt(request.getParameter("id"));
                ItemBean i = dao.getItemById(id);
                request.setAttribute("item", i);
                request.getRequestDispatcher("editItem.jsp").forward(request,response);

            } else if("delete".equals(action)){
                if(!"admin".equals(role)){
                    response.sendRedirect("ItemServlet?action=list");
                    return;
                }
                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteItem(id);
                session.setAttribute("message","Item deleted successfully!");
                response.sendRedirect("ItemServlet?action=list");

            } else{
                response.sendRedirect("ItemServlet?action=list");
            }

        } catch(Exception e){
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if(session==null || session.getAttribute("currentUser")==null){
            response.sendRedirect("login.jsp");
            return;
        }
        UserBean user = (UserBean) session.getAttribute("currentUser");
        String role = user.getRole();

        String action = request.getParameter("action");
        ItemDao dao = new ItemDao();

        try{
            if("add".equals(action)){
                ItemBean i = new ItemBean();
                i.setTitle(request.getParameter("title"));
                i.setAuthor(request.getParameter("author"));
                i.setCategory(request.getParameter("category"));
                i.setPrice(new java.math.BigDecimal(request.getParameter("price")));
                i.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
                i.setDateAdded(Date.valueOf(request.getParameter("dateAdded")));
                dao.addItem(i);
                session.setAttribute("message","Item added successfully!");
                response.sendRedirect("ItemServlet?action=list");

            } else if("edit".equals(action)){
                if(!"admin".equals(role)){
                    response.sendRedirect("ItemServlet?action=list");
                    return;
                }
                ItemBean i = new ItemBean();
                i.setItemId(Integer.parseInt(request.getParameter("itemId")));
                i.setTitle(request.getParameter("title"));
                i.setAuthor(request.getParameter("author"));
                i.setCategory(request.getParameter("category"));
                i.setPrice(new java.math.BigDecimal(request.getParameter("price")));
                i.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
                i.setDateAdded(Date.valueOf(request.getParameter("dateAdded")));
                dao.updateItem(i);
                session.setAttribute("message","Item updated successfully!");
                response.sendRedirect("ItemServlet?action=list");
            } else{
                response.sendRedirect("ItemServlet?action=list");
            }

        } catch(Exception e){
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
