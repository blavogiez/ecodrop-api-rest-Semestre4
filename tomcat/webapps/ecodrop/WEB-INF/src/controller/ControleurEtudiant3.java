package controller;

import java.io.IOException;
import java.io.PrintWriter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JwtManager;

@WebServlet("/etudiants3")
public class ControleurEtudiant3 extends HttpServlet {
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
        String authorization = req.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer"))
            return false;
        // on décode le token
        String token = authorization.substring("Bearer".length()).trim();
        System.out.println(token);

        try {
            Claims claims = JwtManager.decodeJWT(token);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}