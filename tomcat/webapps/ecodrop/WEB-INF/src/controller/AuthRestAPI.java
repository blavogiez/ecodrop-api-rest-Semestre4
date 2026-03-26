package controller;

import java.io.IOException;

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

        String login = req.getParameter("login");
        if (login == null)
            return;
        String password = req.getParameter("password");
        if (password == null)
            return;

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
