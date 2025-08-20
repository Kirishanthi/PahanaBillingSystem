<%@ page import="Bean.UserBean" %>
<%
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if(user==null || !"staff".equals(user.getRole())){
        response.sendRedirect("login.jsp");
        return;
    }
%>

<h2>Add Customer</h2>
<%
    String msg = (String) session.getAttribute("message");
    if(msg!=null){ out.println("<p style='color:green'>"+msg+"</p>"); session.removeAttribute("message"); }
%>

<form action="<%=request.getContextPath()%>/AddCustomerServlet" method="post">
    <input type="hidden" name="action" value="add">
    Account Number: <input type="text" name="accountNumber" required><br>
    Name: <input type="text" name="name" required><br>
    Address: <input type="text" name="address" required><br>
    Telephone: <input type="text" name="telephone" required><br>
    Date Registered: <input type="date" name="dateRegistered" required><br><br>
    <input type="submit" value="Add Customer">
</form>

<a href="AddCustomerServlet?action=list">View Customer List</a>
