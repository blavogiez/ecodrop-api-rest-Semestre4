package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.LoginJdbc;

@WebServlet("/GenererToken1")
public class GenererToken1 extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println("retour d'un GET");

        String login = req.getParameter("login");
        String pwd = req.getParameter("pwd");

        if (LoginJdbc.userExists(login, pwd)) {
            UUID token = UUID.randomUUID();
            LoginJdbc.storeUUID(login, token);

            out.println("<h1>Votre token est :</h1>");
            out.println(token.toString());
        } else {
            out.println("<h1>Vous êts inconnu ici !!</h1>");
        }
    }
}