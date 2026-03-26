package controller;

import java.io.IOException;
import java.util.Base64;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.AuthDAO;
import model.dao.AuthDAOPostgres;
import utils.JwtManager;
import utils.RequestContext;

@WebServlet("/auth/token")
public class AuthRestAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.hasArguments()) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String decoded = new String(Base64.getDecoder().decode(authHeader.substring(6)));
        String[] parts = decoded.split(":", 2);
        if (parts.length != 2) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String login = parts[0];
        String password = parts[1];

        AuthDAO dao = new AuthDAOPostgres();

        if (dao.credentialsReferToExistingAccount(login, password)) {
            String token = JwtManager.createJWT(login);
            ctx.print(token);
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        } else {
            ctx.print("Invalid credentials");
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

    }
}
