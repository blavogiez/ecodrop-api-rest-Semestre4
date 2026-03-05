<%@ page pageEncoding="UTF-8" %>
<%@ page import="java.util.*, model.*" %>
<!DOCTYPE html>
<html>
<head><title>Modifier</title></head>
<body>
<h1>Modifier</h1>
<jsp:include page="menu.jsp"/>

<form method='post' action='Control'>
    <input type='hidden' name='action' value='modifier'>

    <p>Rentrez l'id de partie voulu</p>
    <input type='text' name='pno'><br>

    <p>Sélectionnez les joueurs</p>
    <%
    List<Joueur> joueurs = (List<Joueur>) request.getAttribute("joueurs");
    %>
    <select name='jno1'>
    <% for(Joueur j : joueurs) { %>
        <option value='<%= j.getJno() %>'><%= j.getPseudo() %></option>
    <% } %>
    </select>

    <select name='jno2'>
    <% for(Joueur j : joueurs) { %>
        <option value='<%= j.getJno() %>'><%= j.getPseudo() %></option>
    <% } %>
    </select>

    <p>Rentrez la nouvelle date voulue</p>
    <input type='date' name='date'>

    <p>Sélectionnez le statut</p>
    <select name="statut">
        <option value="">--Veuillez choisir une option--</option>
        <option value="Non commencée">Non commencée</option>
        <option value="En cours">En cours</option>
        <option value="Terminée">Terminée</option>
    </select>

    <input type='submit'>
</form>
</body>
</html>
