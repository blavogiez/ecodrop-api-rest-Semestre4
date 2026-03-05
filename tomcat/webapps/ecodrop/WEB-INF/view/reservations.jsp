<%@ page pageEncoding="UTF-8" %>
<%@ page import="java.util.*, model.*" %>
<!DOCTYPE html>
<html>
<head><title>Lister</title></head>
<body>
<h1>Lister</h1>
<jsp:include page="menu.jsp"/>

<h2>Réservations</h2>
<table>
<% 
List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
int i = 1 ;
for(Reservation reservation : reservations) { %>
    <tr>
    <td>
    <%= reservation.getCno()%>
    </td>
    <td>
    <%= reservation.getHeure()%>
    </td>
    <td>
    <%= reservation.getType_repas()%>
    </td>
    <td>
    <a href='reservations?action=voir&rno=<%= i%>'>Voir</a>
    <td>
    </tr>
    <%-- out.println("<tr>");
    out.println("<td>");
    out.println(reservation);
    out.println("</td>");
    out.println("<td>");
    out.println("<a href='reservations?action=voir&rno="+i+"'>Voir</a>");
    out.println("</td>");
    out.println("</tr>"); --%>
    <% i++;
}
%>
</table>
</body>
</html>
