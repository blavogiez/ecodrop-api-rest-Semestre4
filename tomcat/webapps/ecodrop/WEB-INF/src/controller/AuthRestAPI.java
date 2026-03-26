package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.TokenDAO;
import model.dao.TokenDAOPostgres;
import utils.RequestContext;

import java.io.IOException;

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
        if (login == null) return;
        String password = req.getParameter("password");
        if (password == null) return;

        TokenDAO dao = new TokenDAOPostgres();
        String token = dao.getOrCreateToken(login, password);
        ctx.print(token);
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
