package controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.LoginJdbc;

@WebServlet("/etudiants1")
public class ControleurEtudiant1 extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();

        if (verifToken(req)) {
            out.println("retour d'un GET");
        } else {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
    }

    private boolean verifToken(HttpServletRequest req) {
        String token = req.getParameter("token");
        return LoginJdbc.verifToken(token);
    }
}