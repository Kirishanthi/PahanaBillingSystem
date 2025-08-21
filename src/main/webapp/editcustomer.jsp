<%@ page import="Bean.UserBean, Bean.CustomerBean" %>
<%
    UserBean user = (UserBean) session.getAttribute("currentUser");
    if(user == null){
        response.sendRedirect("login.jsp");
        return;
    }

    // Only admin can edit
    String role = user.getRole();
    if(!"admin".equals(role)){
        response.sendRedirect("login.jsp");
        return;
    }

    CustomerBean customer = (CustomerBean) request.getAttribute("customer");
    if(customer == null){
        response.sendRedirect("CustomerServlet?action=list");
        return;
    }

    String msg = (String) session.getAttribute("message");
    if(msg != null){
        out.println("<p style='color:green'>" + msg + "</p>");
        session.removeAttribute("message");
    }
%>
<title>Pahana Edu - Login</title><link rel="stylesheet" href="<%=request.getContextPath()%>/css/editcustomer.css">

<h2>Edit Customer</h2>

<form action="CustomerServlet" method="post">
    <input type="hidden" name="action" value="edit">
    <input type="hidden" name="customerId" value="<%=customer.getCustomerId()%>">
    
    Account Number: <input type="text" name="accountNumber" value="<%=customer.getAccountNumber()%>" required><br><br>
    Name: <input type="text" name="name" value="<%=customer.getName()%>" required><br><br>
    Address: <input type="text" name="address" value="<%=customer.getAddress()%>"><br><br>
    Telephone: <input type="text" name="telephone" value="<%=customer.getTelephone()%>"><br><br>
    Date Registered: <input type="date" name="dateRegistered" value="<%=customer.getDateRegistered()%>" required><br><br>
    
    <input type="submit" value="Update Customer">
</form>

<a href="CustomerServlet?action=list">Back to Customer List</a>
