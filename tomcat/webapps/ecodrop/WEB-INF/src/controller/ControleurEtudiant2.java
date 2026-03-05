package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.LoginJdbc;

@WebServlet("/etudiants2")
public class ControleurEtudiant2 extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();

        if (verifToken(req)) {
            out.println("retour d'un GET");
        } else {
            res.setHeader("WWW-Authenticate", "Basic realm=\"Connecte toi !!\"");
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);

            return;
        }
    }

    private boolean verifToken(HttpServletRequest req) {
        String authorization = req.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Basic"))
            return false;
        // on décode le token
        String token = authorization.substring("Basic".length()).trim();
        byte[] base64 = Base64.getDecoder().decode(token);
        String decoded = new String(base64, StandardCharsets.UTF_8);
        System.out.println("J'ai décodé : " + decoded);
        int idx = decoded.indexOf(':');
        if (idx < 0)
            return false;
        String login = decoded.substring(0, idx);
        String pwd = decoded.substring(idx + 1);
        return LoginJdbc.userExists(login, pwd);
    }
}